package com.natwest.submersible.navigation.engine;

import com.natwest.submersible.navigation.context.NavigationContext;
import com.natwest.submersible.navigation.domain.Position;
import com.natwest.submersible.navigation.domain.enums.Command;
import com.natwest.submersible.navigation.results.MoveResult;
import com.natwest.submersible.navigation.results.NavigationResult;
import com.natwest.submersible.navigation.strategy.MovementStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * NavigationEngine is responsible for executing a sequence of navigation commands on a probe within a navigation context.
 * It applies each command using the provided MovementStrategy, tracks the path traversed, and returns a NavigationResult
 * containing the final probe state, the path taken, and any failure reason if a command fails.
 * <p>
 * Usage:
 * <pre>
 *     NavigationResult result = navigationEngine.executeCommand(context, commands);
 * </pre>
 * The result will indicate success or failure, the final state, and the full path traversed.
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class NavigationEngine {

    private final MovementStrategy movementStrategy;

    /**
     * Executes a sequence of navigation commands on the probe within the given navigation context.
     * <p>
     * For each command, applies the movement strategy, updates the probe state, and tracks the path traversed.
     * If any command fails, returns a failure NavigationResult with the current state, failure reason, and path so far.
     * On success, returns a NavigationResult with the final state and the full path.
     *
     * @param context  the initial navigation context containing the grid and probe state
     * @param commands the list of navigation commands to execute
     * @return NavigationResult indicating success/failure, final state, and path traversed
     */

    public NavigationResult executeCommand(final NavigationContext context, final List<Command> commands) {
        log.info("Executing command: {} with context: {}", commands, context);

        List<Position> path = new ArrayList<>();

        NavigationContext currentContext = context;
        path.add(currentContext.probeState().position());

        for (Command cmd : commands) {
            log.debug("Processing command: {} with current context: {}", cmd, currentContext);
            MoveResult result = movementStrategy.apply(currentContext, cmd);
            log.debug("Result after applying command {}: {}", cmd, result);
            if (!result.status()) {
                log.warn("Command {} failed with reason: {}", cmd, result.reason());
                return NavigationResult.failure(currentContext.probeState(), "Command " + cmd + " failed: " + result.reason(), path);
            }
            currentContext = currentContext.withState(result.probeState());
            path.add(currentContext.probeState().position());
        }

        return NavigationResult.success(currentContext.probeState(), path);
    }
}
