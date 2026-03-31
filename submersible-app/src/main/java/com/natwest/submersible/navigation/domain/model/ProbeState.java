package com.natwest.submersible.navigation.domain.model;

import com.natwest.submersible.navigation.domain.model.enums.Direction;
import lombok.extern.slf4j.Slf4j;

/**
 * ProbeState represents the immutable state of a probe, including its position and direction on the navigation grid.
 * It provides movement operations (left, right, forward, backward) that return new ProbeState instances reflecting
 * the updated state after the operation. All movement methods log their actions for traceability.
 * <p>
 * Usage:
 * <pre>
 *     ProbeState state = new ProbeState(position, direction);
 *     ProbeState next = state.forward();
 *     ProbeState turned = state.left();
 * </pre>
 */
@Slf4j
public record ProbeState(Position position, Direction direction) {

    private static boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * Returns a new ProbeState after turning left from the current direction.
     *
     * @return ProbeState with the same position and new direction after turning left
     */
    public ProbeState left() {
        Direction newDirection = direction.left();
        if (isDebugEnabled()) {
            log.debug("Turning left from direction {} to {}", direction, newDirection);
        }
        return new ProbeState(position, newDirection);
    }

    /**
     * Returns a new ProbeState after turning right from the current direction.
     *
     * @return ProbeState with the same position and new direction after turning right
     */
    public ProbeState right() {
        Direction newDirection = direction.right();
        if (isDebugEnabled()) {
            log.debug("Turning right from direction {} to {}", direction, newDirection);
        }
        return new ProbeState(position, newDirection);
    }

    /**
     * Returns a new ProbeState after moving forward in the current direction.
     *
     * @return ProbeState with updated position after moving forward
     */
    public ProbeState forward() {
        if (isDebugEnabled()) {
            log.debug("Moving forward from position {} in direction {}", position, direction);
        }
        ProbeState newState = new ProbeState(position.move(direction), direction);
        if (isDebugEnabled()) {
            log.debug("New position after moving forward: {}", newState.position());
        }
        return newState;
    }

    /**
     * Returns a new ProbeState after moving backward (opposite of current direction).
     *
     * @return ProbeState with updated position after moving backward
     */
    public ProbeState backward() {
        if (isDebugEnabled()) {
            log.debug("Moving backward from position {} in direction {}", position, direction);
        }
        Direction oppositeDirection = switch (direction) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
        };
        ProbeState newState = new ProbeState(position.move(oppositeDirection), direction);
        if (isDebugEnabled()) {
            log.debug("New position after moving backward: {} ", newState.position());
        }
        return newState;
    }
}
