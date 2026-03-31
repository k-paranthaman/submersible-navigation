package com.natwest.submersible.navigation.api;

import com.natwest.submersible.navidator.api.PathPlanningApi;
import com.natwest.submersible.navidator.model.PathPlanningRequest;
import com.natwest.submersible.navidator.model.PathPlanningResponse;
import com.natwest.submersible.navigation.service.PathPlanningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * PathPlanningController exposes REST endpoints for path planning operations.
 * It implements the PathPlanningApi interface and is responsible for handling requests to generate
 * navigation command sequences to reach a target state from the current state.
 * <p>
 * Usage:
 * <pre>
 *     POST /api/v1/navigation/plan
 * </pre>
 * The controller validates requests and returns the planned path and commands.
 */
@RestController
@RequiredArgsConstructor
public class PathPlanningController implements PathPlanningApi {

    private final PathPlanningService pathPlanningService;

    /**
     * Handles the planning of a navigation path for a probe.
     * Accepts a validated PathPlanningRequest, delegates to the service layer, and returns the planned path and commands.
     *
     * @param pathPlanningRequest the path planning request payload
     * @return ResponseEntity containing the PathPlanningResponse
     */
    @Override
    public ResponseEntity<PathPlanningResponse> planPath(@Valid @RequestBody PathPlanningRequest pathPlanningRequest) {
        return ResponseEntity.ok(pathPlanningService.planPath(pathPlanningRequest));
    }

}
