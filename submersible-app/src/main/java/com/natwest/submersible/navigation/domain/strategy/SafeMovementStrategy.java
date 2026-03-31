package com.natwest.submersible.navigation.domain.strategy;


import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import com.natwest.submersible.navigation.domain.validator.ValidatorChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Movement strategy that applies navigation commands to the probe state with validation.
 * <p>
 * Implements the {@link MovementStrategy} interface to execute movement commands (FORWARD, BACKWARD, LEFT, RIGHT)
 * on the current probe state, producing a new state and validating it using a {@link ValidatorChain}.
 * <ul>
 *   <li>Logs the command and context before and after applying the movement.</li>
 *   <li>Delegates movement logic to the {@link ProbeState} methods based on the command.</li>
 *   <li>Creates a new {@link NavigationContext} with the updated state and validates it using the validator chain.</li>
 *   <li>Returns a {@link MoveResult} representing the outcome of the move and validation.</li>
 * </ul>
 * Used by navigation engines to ensure that each movement is both executed and validated for safety (e.g., bounds, obstacles).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SafeMovementStrategy implements MovementStrategy {

    private final ValidatorChain validatorChain;

    /**
     * Applies the safe movement strategy to the given navigation context and command.
     * <p>
     * Updates the probe state according to the command, creates a new context, and validates the result.
     *
     * @param context the current navigation context
     * @param command the movement command to execute
     * @return the result of the movement operation as a {@link MoveResult}
     */
    @Override
    public MoveResult apply(NavigationContext context, Command command) {
        var state = context.probeState();
        log.debug("Applying SafeMovementStrategy with command: {} and context: {}", command, context);

        ProbeState nextState = switch (command) {
            case FORWARD -> state.forward();
            case BACKWARD -> state.backward();
            case LEFT -> state.left();
            case RIGHT -> state.right();
        };

        log.debug("Next state after applying command {}: {}", command, nextState);

        NavigationContext newContext = context.withState(nextState);

        return validatorChain.validate(newContext);
    }
}
