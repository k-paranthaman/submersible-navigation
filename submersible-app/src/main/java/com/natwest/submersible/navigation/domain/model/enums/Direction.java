package com.natwest.submersible.navigation.domain.model.enums;

public enum Direction {

    NORTH, SOUTH, EAST, WEST;

    /**
     * Returns the direction after turning left from the current direction.
     *
     * @return the new Direction after a left turn
     */
    public Direction left(){
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case EAST -> NORTH;
            case WEST -> SOUTH;
        };
    }

    /**
     * Returns the direction after turning right from the current direction.
     *
     * @return the new Direction after a right turn
     */
    public Direction right(){
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
            case WEST -> NORTH;
        };
    }
}
