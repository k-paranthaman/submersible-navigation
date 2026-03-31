package com.natwest.submersible.navigation.domain.validator;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validator that checks if the probe's position is within the boundaries of the navigation grid after a movement operation.
 * <p>
 * Implements the {@link Validator} interface to ensure that the probe does not move outside the defined grid dimensions.
 * <ul>
 *   <li>Logs the probe's position and grid dimensions during validation for traceability.</li>
 *   <li>If the probe is out of bounds, returns a failed {@link MoveResult} with an appropriate reason.</li>
 *   <li>If the probe is within bounds, returns a successful {@link MoveResult}.</li>
 * </ul>
 * <p>
 * Used in the validation chain to enforce boundary constraints as part of safe navigation.
 */
@Component
@Slf4j
public class BoundaryValidator implements Validator {

    /**
     * Validates that the probe's current position is within the grid boundaries.
     * <p>
     * If the probe is out of bounds, returns a failed {@link MoveResult}; otherwise, returns success.
     *
     * @param context the navigation context containing the grid and probe state
     * @return a {@link MoveResult} indicating success or failure due to boundary constraints
     */
    @Override
    public MoveResult validate(final NavigationContext context) {

        var grid = context.grid();
        var probeState = context.probeState();
        log.debug("Validating boundaries for position: {} in grid {}X{}", probeState.position() , grid.width(), grid.height());
        if(!grid.isWithinBounds(probeState.position())) {
            log.warn("Probe is out of bounds at position: {} in grid {}X{}", probeState.position(), grid.width(), grid.height());
            return MoveResult.failure(probeState, "Probe is out of bounds at position: " + probeState.position());

        }
        log.debug("Position {} is within bounds for grid {}X{}", probeState.position(), grid.width(), grid.height());

        return MoveResult.success(probeState);
    }
}
