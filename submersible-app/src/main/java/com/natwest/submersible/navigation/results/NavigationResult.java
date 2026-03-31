package com.natwest.submersible.navigation.results;

import com.natwest.submersible.navigation.domain.Position;
import com.natwest.submersible.navigation.domain.ProbeState;

import java.util.List;

/**
 * Represents the result of a navigation operation, including the probe's final state and the path taken.
 * <p>
 * Encapsulates the outcome of a navigation attempt, such as pathfinding or movement sequences, and provides details about:
 * <ul>
 *   <li><b>status</b>: Indicates if the navigation was successful (true) or failed (false).</li>
 *   <li><b>reason</b>: Provides a failure reason if the navigation was unsuccessful; null if successful.</li>
 *   <li><b>probeState</b>: The state of the probe after navigation completes.</li>
 *   <li><b>path</b>: The list of positions traversed during navigation, including the final position.</li>
 * </ul>
 * <p>
 * Provides static factory methods for creating success and failure results.
 * Used by navigation logic and controllers to communicate navigation outcomes, probe state transitions, and the computed path.
 */
public record NavigationResult(boolean status, String reason, ProbeState probeState, List<Position> path) {

    /**
     * Creates a successful navigation result with the given probe state and path.
     *
     * @param probeState the resulting probe state after successful navigation
     * @param path the list of positions traversed
     * @return a successful {@link NavigationResult}
     */
    public static NavigationResult success(final ProbeState probeState, final List<Position> path) {
        return new NavigationResult(Boolean.TRUE, null, probeState, path);
    }

    /**
     * Creates a failed navigation result with the given probe state, failure reason, and path.
     *
     * @param probeState the resulting probe state after failed navigation
     * @param reason the reason for the navigation failure
     * @param path the list of positions traversed up to failure
     * @return a failed {@link NavigationResult}
     */
    public static NavigationResult failure(final ProbeState probeState, final String reason, final List<Position> path) {
        return new NavigationResult(Boolean.FALSE, reason, probeState, path);
    }
}
