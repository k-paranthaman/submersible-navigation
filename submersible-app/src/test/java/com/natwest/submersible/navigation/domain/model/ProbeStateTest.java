package com.natwest.submersible.navigation.domain.model;

import com.natwest.submersible.navigation.domain.model.enums.Direction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class ProbeStateTest {

    private final Position initialPosition = new Position(5, 5);

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testLeft(Direction direction) {
        // Arrange
        ProbeState probeState = new ProbeState(initialPosition, direction);

        // Act
        ProbeState newState = probeState.left();

        // Assert
        Direction expectedLeft = switch (direction) {
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.NORTH;
        };
        assertEquals(expectedLeft, newState.direction());
        assertEquals(initialPosition, newState.position());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testRight(Direction direction) {
        // Arrange
        ProbeState probeState = new ProbeState(initialPosition, direction);

        // Act
        ProbeState newState = probeState.right();

        // Assert
        Direction expectedRight = switch (direction) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
        assertEquals(expectedRight, newState.direction());
        assertEquals(initialPosition, newState.position());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testForward(Direction direction) {
        // Arrange
        ProbeState probeState = new ProbeState(initialPosition, direction);

        // Act
        ProbeState newState = probeState.forward();

        // Assert
        Position expectedPosition = switch (direction) {
            case NORTH -> new Position(5, 6);
            case SOUTH -> new Position(5, 4);
            case EAST -> new Position(6, 5);
            case WEST -> new Position(4, 5);
        };
        assertEquals(direction, newState.direction());
        assertEquals(expectedPosition, newState.position());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testBackward(Direction direction) {
        // Arrange
        ProbeState probeState = new ProbeState(initialPosition, direction);

        // Act
        ProbeState newState = probeState.backward();

        // Assert
        Position expectedPosition = switch (direction) {
            case NORTH -> new Position(5, 4);
            case SOUTH -> new Position(5, 6);
            case EAST -> new Position(4, 5);
            case WEST -> new Position(6, 5);
        };
        assertEquals(direction, newState.direction());
        assertEquals(expectedPosition, newState.position());
    }
}
