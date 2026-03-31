package com.natwest.submersible.navigation.service;

import com.natwest.submersible.navidator.model.*;
import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.service.support.NavigationSupport;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import com.natwest.submersible.navigation.service.mapper.NavigationGridMapper;
import com.natwest.submersible.navigation.service.mapper.PositionMapper;
import com.natwest.submersible.navigation.service.mapper.ProbeStateMapper;
import com.natwest.submersible.navigation.service.parser.CommandParser;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import com.natwest.submersible.navigation.domain.results.NavigationResult;
import com.natwest.submersible.navigation.domain.validator.ValidatorChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NavigationService {

    private final NavigationSupport navigationSupport;
    private final ValidatorChain validatorChain;

    private final NavigationGridMapper gridMapper;
    private final ProbeStateMapper probeStateMapper;

    public NavigationResponse executeNavigation(final NavigationRequest navigationRequest) {

        log.info("Received navigation request: {}", navigationRequest);

        final NavigationGrid navigationGrid = gridMapper.toDomain(navigationRequest.getGrid());

        final ProbeState probeState = probeStateMapper.toDomain(navigationRequest.getProbeState());

        final List<Command> commands = CommandParser.parseCommands(navigationRequest.getCommands());

        final NavigationContext context = new NavigationContext(navigationGrid, probeState);

        isPositionValid(context);

        final NavigationResult result = navigationSupport.executeCommand(context, commands);

        return toResponse(result);
    }

    private void isPositionValid(final NavigationContext context) {
        MoveResult validationResult = validatorChain.validate(context);
        if (!validationResult.status()) {
            log.warn("Initial position validation failed: {}", validationResult.reason());
            throw new ProbeException(ErrorCode.INVALID_POSITION, "Invalid initial position: " + validationResult.reason());
        }
    }


    private NavigationResponse toResponse(final NavigationResult result) {
        log.debug("Mapping NavigationResult to NavigationResponse: {}", result);
        final StateDto stateDto = probeStateMapper.toModel(result.probeState());
        final Status status = result.status() ? Status.SUCCESS : Status.FAILURE;

        final List<PositionDto> path = result.path().
                stream().map(PositionMapper::toModel).toList();

        final NavigationResponse response = new NavigationResponse(status, path);
        response.setFinalPosition(stateDto);
        response.setReason(result.reason());

        log.debug("Mapped NavigationResponse: {}", response);
        return response;
    }
}
