package com.natwest.submersible.navigation.domain.context;

import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.ProbeState;
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

    private static boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * Returns a new NavigationContext with the same grid and a new probe state.
     * This method is used to update the probe's state immutably.
     *
     * @param probeState the new probe state
     * @return a new NavigationContext with the updated probe state
     */
    public NavigationContext withState(final ProbeState probeState) {
        if (isDebugEnabled()) {
            log.debug("Updating NavigationContext with new ProbeState: {}", probeState);
        }
        return new NavigationContext(this.grid, probeState);
    }

}
