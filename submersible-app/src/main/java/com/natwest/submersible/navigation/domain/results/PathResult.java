package com.natwest.submersible.navigation.domain.results;

import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.enums.Command;

import java.util.List;

/**
 * Represents the result of a pathfinding or path planning operation in the navigation system.
 * <p>
 * Encapsulates the outcome of computing a path for the probe, including the sequence of commands to follow,
 * the list of obstracles traversed, the overall status, and a failure reason if applicable.
 * <ul>
 *   <li><b>commands</b>: The list of navigation commands to execute for the computed path (may be null on failure).</li>
 *   <li><b>path</b>: The list of obstracles traversed or planned, including the final destination.</li>
 *   <li><b>status</b>: Indicates if the pathfinding was successful (true) or failed (false).</li>
 *   <li><b>reason</b>: Provides a failure reason if the operation was unsuccessful; null if successful.</li>
 * </ul>
 * <p>
 * Provides static factory methods for creating success and failure results.
 * Used by navigation logic and controllers to communicate path planning outcomes, including the planned route and any errors encountered.
 */
public record PathResult(List<Command> commands, List<Position> path, boolean status, String reason) {

    /**
     * Creates a successful path result with the given commands and path.
     *
     * @param commands the list of navigation commands for the path
     * @param path the list of obstracles traversed or planned
     * @return a successful {@link PathResult}
     */
    public static PathResult success(final List<Command> commands, final List<Position> path) {
        return new PathResult(commands, path, Boolean.TRUE, null);
    }

    /**
     * Creates a failed path result with the given path and failure reason.
     *
     * @param path the list of obstracles traversed up to failure
     * @param reason the reason for the pathfinding failure
     * @return a failed {@link PathResult}
     */
    public static PathResult failure( final List<Position> path, final String reason) {
        return new PathResult(null, path, Boolean.FALSE, reason);
    }
}
