package com.natwest.submersible.navigation.service;

import com.natwest.submersible.navidator.model.PathPlanningRequest;
import com.natwest.submersible.navidator.model.PathPlanningResponse;
import com.natwest.submersible.navidator.model.PositionDto;
import com.natwest.submersible.navidator.model.Status;
import com.natwest.submersible.navigation.context.NavigationContext;
import com.natwest.submersible.navigation.domain.NavigationGrid;
import com.natwest.submersible.navigation.domain.ProbeState;
import com.natwest.submersible.navigation.engine.PathEngine;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import com.natwest.submersible.navigation.mapper.NavigationGridMapper;
import com.natwest.submersible.navigation.mapper.PositionMapper;
import com.natwest.submersible.navigation.mapper.ProbeStateMapper;
import com.natwest.submersible.navigation.parser.CommandParser;
import com.natwest.submersible.navigation.results.MoveResult;
import com.natwest.submersible.navigation.results.PathResult;
import com.natwest.submersible.navigation.validator.ValidatorChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service for handling path planning requests and mapping results to API responses.
 * <p>
 * This component orchestrates the path planning workflow:
 * <ul>
 *   <li>Receives {@link PathPlanningRequest} objects from controllers.</li>
 *   <li>Maps request DTOs to domain models using mappers.</li>
 *   <li>Delegates pathfinding logic to the {@link PathEngine}.</li>
 *   <li>Maps {@link PathResult} domain results to {@link PathPlanningResponse} DTOs for API output.</li>
 *   <li>Logs key steps for traceability and debugging.</li>
 * </ul>
 * Used by controllers to provide path planning functionality for the navigation system.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PathPlanningService {

    private final PathEngine pathEngine;
    private final ValidatorChain validatorChain;


    /**
     * Plans a path from the current probe state to the target state using the provided grid.
     * <p>
     * Maps the request to domain models, invokes the path engine, and returns a mapped response.
     *
     * @param pathPlanningRequest the path planning request containing grid, current, and target state
     * @return a {@link PathPlanningResponse} containing the planned commands, path, and status
     */
    public PathPlanningResponse planPath(final PathPlanningRequest pathPlanningRequest) {

        log.info("Received path planning request: {}", pathPlanningRequest);

        final NavigationGrid navigationGrid = NavigationGridMapper.toDomain(pathPlanningRequest.getGrid());

        final ProbeState starter = ProbeStateMapper.toDomain(pathPlanningRequest.getCurrentState());

        final ProbeState target = ProbeStateMapper.toDomain(pathPlanningRequest.getTargetState());

        final NavigationContext context = new NavigationContext(navigationGrid, starter);

        final PathResult result = pathEngine.findPath(context, target);

        return toResponse(result);
    }

    /**
     * Maps a {@link PathResult} domain object to a {@link PathPlanningResponse} DTO.
     * <p>
     * Converts the list of commands and path to their DTO representations and sets the response status.
     *
     * @param result the path planning result from the domain layer
     * @return a mapped {@link PathPlanningResponse}
     */
    private PathPlanningResponse toResponse(final PathResult result) {
        log.debug("Mapping PathResult to PathPlanningResponse: {}", result);
        
        final String commands = CommandParser.convertComments(result.commands());
        final Status status = result.status() ? Status.SUCCESS : Status.FAILURE;


        final PathPlanningResponse response = new PathPlanningResponse(commands);
        response.setStatus(status);
        
        final List<PositionDto> path = result.path().
                stream().map(PositionMapper::toModel).toList();
        response.setPath(path);
        log.debug("Mapped PathPlanningResponse: {}", response);
        return response;
    }



}
