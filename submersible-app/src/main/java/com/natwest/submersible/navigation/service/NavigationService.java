package com.natwest.submersible.navigation.service;

import com.natwest.submersible.navidator.model.*;
import com.natwest.submersible.navigation.context.NavigationContext;
import com.natwest.submersible.navigation.domain.NavigationGrid;
import com.natwest.submersible.navigation.domain.ProbeState;
import com.natwest.submersible.navigation.domain.enums.Command;
import com.natwest.submersible.navigation.engine.NavigationEngine;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import com.natwest.submersible.navigation.mapper.NavigationGridMapper;
import com.natwest.submersible.navigation.mapper.PositionMapper;
import com.natwest.submersible.navigation.mapper.ProbeStateMapper;
import com.natwest.submersible.navigation.parser.CommandParser;
import com.natwest.submersible.navigation.results.MoveResult;
import com.natwest.submersible.navigation.results.NavigationResult;
import com.natwest.submersible.navigation.validator.ValidatorChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NavigationService {

    private final NavigationEngine navigationEngine;
    private final ValidatorChain validatorChain;

    public NavigationResponse executeNavigation(final NavigationRequest navigationRequest) {

        log.info("Received navigation request: {}", navigationRequest);

        final NavigationGrid navigationGrid = NavigationGridMapper.toDomain(navigationRequest.getGrid());

        final ProbeState probeState = ProbeStateMapper.toDomain(navigationRequest.getProbeState());

        final List<Command> commands = CommandParser.parseCommands(navigationRequest.getCommands());

        final NavigationContext context = new NavigationContext(navigationGrid, probeState);

        isPositionValid(context);

        final NavigationResult result = navigationEngine.executeCommand(context, commands);

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
        final StateDto stateDto = ProbeStateMapper.toModel(result.probeState());
        final Status status = result.status() ? Status.SUCCESS : Status.FAILURE;

        final List<PositionDto> path = result.path().
                stream().map(PositionMapper::toModel).toList();

        final NavigationResponse response = new NavigationResponse(status, path);
        response.setFinalPosition(stateDto);

        log.debug("Mapped NavigationResponse: {}", response);
        return response;
    }
}
