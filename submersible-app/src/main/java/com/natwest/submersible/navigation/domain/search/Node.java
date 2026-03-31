package com.natwest.submersible.navigation.domain.search;

import com.natwest.submersible.navigation.domain.ProbeState;
import com.natwest.submersible.navigation.domain.enums.Command;

import java.util.List;

/**
 * Node is a value object used in pathfinding algorithms (such as BFS) to represent a probe state and the sequence of commands
 * taken to reach that state. It encapsulates the current probe state and the path (as a list of commands) from the start node.
 * <p>
 * Usage:
 * <pre>
 *     Node node = new Node(probeState, commands);
 *     ProbeState state = node.probeState();
 *     List<Command> path = node.commands();
 * </pre>
 */

public record Node(ProbeState probeState, List<Command> commands) {
}
