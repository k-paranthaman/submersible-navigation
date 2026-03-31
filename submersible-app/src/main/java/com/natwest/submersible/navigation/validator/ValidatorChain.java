package com.natwest.submersible.navigation.validator;


import com.natwest.submersible.navigation.context.NavigationContext;
import com.natwest.submersible.navigation.results.MoveResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Executes a chain of {@link Validator} strategies to validate navigation context after movement operations.
 * <p>
 * This component applies a list of validators in sequence, stopping at the first failure and returning its result.
 * <ul>
 *   <li>Maintains an ordered list of {@link Validator} instances to enforce multiple validation rules (e.g., bounds, obstacles).</li>
 *   <li>Logs each validation step for traceability and debugging.</li>
 *   <li>Returns the first failed {@link MoveResult}, or a success result if all validators pass.</li>
 * </ul>
 * <p>
 * Used by movement strategies to ensure that all navigation constraints are checked after each move.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorChain {

    private final List<Validator> validators;

    /**
     * Validates the given navigation context by applying all validators in sequence.
     * <p>
     * Stops and returns immediately if any validator fails; otherwise returns a success result.
     *
     * @param context the navigation context to validate
     * @return a {@link MoveResult} representing the outcome of the validation chain
     */
    public MoveResult validate(final NavigationContext context) {
        log.debug("Starting validation chain for context: {}", context);
        for (Validator validator : validators) {
            log.debug("Executing validator: {}", validator.getClass().getSimpleName());
            MoveResult result = validator.validate(context);
            if (!result.status()) {
                log.warn("Validation failed at {}: {}", validator.getClass().getSimpleName(), result.reason());
                return result;
            }
        }
        return MoveResult.success(context.probeState());
    }
}
