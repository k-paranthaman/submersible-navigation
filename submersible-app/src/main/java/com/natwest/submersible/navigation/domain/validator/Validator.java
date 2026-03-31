package com.natwest.submersible.navigation.domain.validator;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.results.MoveResult;

/**
 * Strategy interface for validating navigation context and movement results.
 * <p>
 * Implementations of this interface encapsulate validation logic for probe movements, such as checking boundaries, obstacles,
 * or other domain-specific rules after a movement command is applied.
 * <ul>
 *   <li>Defines a single method {@link #validate(NavigationContext)} to perform validation on the current context.</li>
 *   <li>Used in validator chains to compose multiple validation strategies for robust movement safety.</li>
 *   <li>Returns a {@link MoveResult} indicating whether the move is valid or failed, with an appropriate reason.</li>
 * </ul>
 * <p>
 * Typical usage involves chaining multiple Validator implementations to enforce all navigation constraints after each move.
 */
public interface Validator {

    /**
     * Validates the given navigation context after a movement operation.
     * <p>
     * Implementations check for domain-specific constraints (e.g., bounds, obstacles) and return a {@link MoveResult}
     * indicating success or failure.
     *
     * @param context the navigation context to validate
     * @return a {@link MoveResult} representing the outcome of the validation
     */
    MoveResult validate(final NavigationContext context);
}
