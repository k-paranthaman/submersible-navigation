package com.natwest.submersible.navigation.results;


import com.natwest.submersible.navigation.domain.ProbeState;

/**
 * Represents the result of a probe movement operation in the navigation system.
 * <p>
 * Encapsulates the outcome of a move attempt, including whether it was successful, the reason for failure (if any),
 * and the resulting {@link ProbeState} after the move.
 * <ul>
 *   <li><b>status</b>: Indicates if the move was successful (true) or failed (false).</li>
 *   <li><b>reason</b>: Provides a failure reason if the move was unsuccessful; null if successful.</li>
 *   <li><b>probeState</b>: The state of the probe after the move attempt.</li>
 * </ul>
 * <p>
 * Provides static factory methods for creating success and failure results.
 * Used by navigation logic and controllers to communicate move outcomes and probe state transitions.
 */
public record MoveResult(boolean status, String reason, ProbeState probeState) {

    /**
     * Creates a successful move result with the given probe state.
     *
     * @param probeState the resulting probe state after a successful move
     * @return a successful {@link MoveResult}
     */
    public static MoveResult success(final ProbeState probeState) {
        return new MoveResult(Boolean.TRUE, null, probeState);
    }

    /**
     * Creates a failed move result with the given probe state and failure reason.
     *
     * @param probeState the resulting probe state after a failed move
     * @param reason the reason for the move failure
     * @return a failed {@link MoveResult}
     */
    public static MoveResult failure(final ProbeState probeState, final String reason) {
        return new MoveResult(Boolean.FALSE, reason, probeState);
    }
}
