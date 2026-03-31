package com.natwest.submersible.navigation.strategy;

import com.natwest.submersible.navigation.context.NavigationContext;
import com.natwest.submersible.navigation.domain.enums.Command;
import com.natwest.submersible.navigation.results.MoveResult;

/**
 * Strategy interface for executing movement commands in the navigation system.
 * <p>
 * Implementations of this interface encapsulate the logic for handling specific movement behaviors (e.g., move forward, turn left, turn right)
 * based on the current navigation context and a given command.
 * <ul>
 *   <li>Defines a single method {@link #apply(NavigationContext, Command)} to perform the movement operation.</li>
 *   <li>Used by navigation engines to delegate movement logic according to the selected strategy.</li>
 *   <li>Promotes the Open/Closed Principle by allowing new movement types to be added without modifying existing code.</li>
 * </ul>
 * <p>
 * Typical usage involves injecting or selecting a concrete MovementStrategy implementation and invoking {@code apply} with the current context and command.
 */
public interface MovementStrategy {

    /**
     * Applies the movement strategy to the given navigation context and command.
     * <p>
     * Implementations perform the movement logic (e.g., updating position, direction, or state) and return the result.
     *
     * @param context the current navigation context (probe state, grid, etc.)
     * @param command the movement command to execute
     * @return the result of the movement operation as a {@link MoveResult}
     */
    MoveResult apply(final NavigationContext context, final Command command);
}
