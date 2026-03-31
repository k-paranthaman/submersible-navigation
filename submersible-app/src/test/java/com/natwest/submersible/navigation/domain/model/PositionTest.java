package com.natwest.submersible.navigation.domain.model;

import com.natwest.submersible.navigation.domain.model.enums.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private final Position initialPosition = new Position(5, 5);

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testMove(Direction direction) {
        // Act
        Position newPosition = initialPosition.move(direction);

        // Assert
        switch (direction) {
            case NORTH -> assertEquals(new Position(5, 6), newPosition);
            case SOUTH -> assertEquals(new Position(5, 4), newPosition);
            case EAST -> assertEquals(new Position(6, 5), newPosition);
            case WEST -> assertEquals(new Position(4, 5), newPosition);
        }
    }
}
