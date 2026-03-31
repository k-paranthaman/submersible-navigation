package com.natwest.submersible.navigation.context;

import com.natwest.submersible.navigation.domain.NavigationGrid;
import com.natwest.submersible.navigation.domain.ProbeState;
import lombok.extern.slf4j.Slf4j;

/**
 * NavigationContext encapsulates the current navigation environment for the probe.
 * It holds the navigation grid and the current probe state (position and direction).
 * This record is immutable; to update the probe state, use the withState method to create a new instance.
 * <p>
 * Usage:
 * <pre>
 *     NavigationContext context = new NavigationContext(grid, probeState);
 *     NavigationContext updated = context.withState(newProbeState);
 * </pre>
 */
@Slf4j
public record NavigationContext(NavigationGrid grid, ProbeState probeState) {

    /**
     * Returns a new NavigationContext with the same grid and a new probe state.
     * This method is used to update the probe's state immutably.
     *
     * @param probeState the new probe state
     * @return a new NavigationContext with the updated probe state
     */
    public NavigationContext withState(final ProbeState probeState) {
        log.debug("Updating NavigationContext with new ProbeState: {}", probeState);
        return new NavigationContext(this.grid, probeState);
    }

}
