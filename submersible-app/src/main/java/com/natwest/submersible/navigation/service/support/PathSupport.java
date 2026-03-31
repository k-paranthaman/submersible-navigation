package com.natwest.submersible.navigation.service.support;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.domain.search.Node;
import com.natwest.submersible.navigation.domain.search.VisitedNode;
import com.natwest.submersible.navigation.service.mapper.PositionMapper;
import com.natwest.submersible.navigation.domain.results.PathResult;
import com.natwest.submersible.navigation.domain.strategy.MovementStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.natwest.submersible.navigation.domain.model.enums.Command.*;

/**
 * The PathEngine class is responsible for finding a path for the probe from its current state to a target state.
 * <p>
 * Uses a breadth-first search (BFS) algorithm to explore possible probe movements and identifies a sequence of commands
 * that will lead the probe to the desired location and orientation. The engine:
 * <ul>
 *   <li>Explores all possible moves (FORWARD, LEFT, RIGHT) from the current state using a queue-based BFS.</li>
 *   <li>Tracks visited obstracles and directions to avoid cycles and redundant exploration.</li>
 *   <li>Delegates movement logic to a {@link MovementStrategy} to apply domain-specific movement and validation.</li>
 *   <li>Returns a {@link PathResult} containing the command sequence and path if the target is reachable, or a failure result otherwise.</li>
 *   <li>Logs key steps for traceability and debugging.</li>
 * </ul>
 * Used by services to provide path planning and navigation functionality for the probe.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PathSupport {

    private static final List<Command> COMMANDS = List.of(FORWARD, LEFT, RIGHT);
    private final MovementStrategy movementStrategy;

    /**
     * Finds a path from the current probe state to the target state using BFS.
     * <p>
     * Explores all possible moves, applies movement strategy, and returns the command sequence and path if successful.
     *
     * @param context the navigation context containing the grid and starting probe state
     * @param target the target probe state (position and direction)
     * @return a {@link PathResult} containing the command sequence and path, or a failure result if unreachable
     */
    public PathResult findPath(final NavigationContext context, final ProbeState target) {
        log.info("Finding path from {} to target {}", context.probeState(), target);

        Queue<Node> queue = new LinkedList<>();

        ProbeState starter = context.probeState();
        queue.add(new Node(starter, List.of()));

        Set<VisitedNode> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            ProbeState currentState = currentNode.probeState();


            log.debug("Exploring node with state: {} and commands: {}", currentState, currentNode.commands());

            if (isTargetReached(currentState, target)) {
                log.info("Found target state: {} with path: {}", currentState, currentNode.commands());
                final List<Position> path = visited.stream().map(PositionMapper::toDomain).toList();
                log.debug("Visited obstracles during search: {}", path);
                return PathResult.success(currentNode.commands(), path);
            }

            // Visited checker
            VisitedNode key = new VisitedNode(currentState.position(), currentState.direction());
            if (!visited.add(key)) {
                log.debug("Already visited state: {}. Skipping.", currentState);
                continue;
            }

            for (Command command : COMMANDS) {

                log.debug("Applying command: {} to state: {}", command, currentState);

                var moveResult = movementStrategy.apply(context.withState(currentState), command);
                if (!moveResult.status()) {
                    log.debug("Invalid move from state: {} using command: {}. Reason: {}", currentState, command, moveResult.reason());
                    continue;
                }

                ProbeState nextState = moveResult.probeState();
                List<Command> newCommand = new LinkedList<>(currentNode.commands());
                newCommand.add(command);

                queue.add(new Node(nextState, newCommand));

                log.debug("Enqueued new node with state: {} and path: {}", nextState, newCommand);
            }
        }
        log.warn("No path found from {} to target {}", context.probeState(), target);

        final List<Position> path = visited.stream().map(PositionMapper::toDomain).toList();
        log.debug("Visited obstracles during search: {}", path);

        return PathResult.failure(path, "Target unreachable");
    }

    /**
     * Checks if the current probe state matches the target probe state (position and direction).
     *
     * @param current The current probe state.
     * @param target The target probe state.
     * @return true if position and direction match, false otherwise.
     */
    private boolean isTargetReached(final ProbeState current, final ProbeState target) {
        return current.position().equals(target.position()) && current.direction() == target.direction();
    }
}
