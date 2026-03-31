package com.natwest.submersible.navigation.domain;


import com.natwest.submersible.navigation.domain.enums.Direction;
import lombok.extern.slf4j.Slf4j;

/**
 * Position represents an immutable coordinate (x, y) on the navigation grid.
 * It provides a movement operation to calculate a new position based on a direction.
 * All movement actions are logged for traceability.
 * <p>
 * Usage:
 * <pre>
 *     Position pos = new Position(1, 2);
 *     Position next = pos.move(Direction.NORTH);
 * </pre>
 */
@Slf4j
public record Position(int x, int y) {

    /**
     * Returns a new Position after moving one unit in the specified direction.
     * The movement does not mutate the current Position instance.
     *
     * @param direction the direction to move (NORTH, SOUTH, EAST, WEST)
     * @return a new Position after moving in the given direction
     */
    public Position move(Direction direction){
        Position newPosition = switch (direction) {
            case NORTH -> new Position(x, y + 1);
            case SOUTH -> new Position(x, y - 1);
            case EAST -> new Position(x + 1, y);
            case WEST -> new Position(x - 1, y);
        };
        log.debug("Moving from position {} to {} in direction {}", this, newPosition, direction);
        return newPosition;

    }

}
