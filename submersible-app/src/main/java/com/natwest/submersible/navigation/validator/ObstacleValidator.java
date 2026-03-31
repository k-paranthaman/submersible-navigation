package com.natwest.submersible.navigation.validator;

import com.natwest.submersible.navigation.context.NavigationContext;
import com.natwest.submersible.navigation.results.MoveResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validator that checks for obstacles at the probe's current position after a movement operation.
 * <p>
 * Implements the {@link Validator} interface to ensure that the probe does not move onto a grid cell containing an obstacle.
 * <ul>
 *   <li>Logs the probe state and validation process for traceability.</li>
 *   <li>If an obstacle is detected at the probe's position, returns a failed {@link MoveResult} with an appropriate reason.</li>
 *   <li>If no obstacle is present, returns a successful {@link MoveResult}.</li>
 * </ul>
 * <p>
 * Used in the validation chain to enforce obstacle avoidance as part of safe navigation.
 */
@Component
@Slf4j
public class ObstacleValidator implements Validator {

    /**
     * Validates that the probe's current position does not contain an obstacle.
     * <p>
     * If an obstacle is detected, returns a failed {@link MoveResult}; otherwise, returns success.
     *
     * @param context the navigation context containing the grid and probe state
     * @return a {@link MoveResult} indicating success or failure due to obstacle presence
     */
    @Override
    public MoveResult validate(final NavigationContext context) {

        final var grid = context.grid();
        final var probeState = context.probeState();

        log.debug("Validating obstacle for probe state: {}", probeState);
        if (grid.isObstacle(probeState.position())) {
            log.warn("Obstacle detected at position: {}", probeState.position());
            return MoveResult.failure(probeState, "Obstacle detected at position: " + probeState.position());
        }

        return MoveResult.success(probeState);
    }
}
