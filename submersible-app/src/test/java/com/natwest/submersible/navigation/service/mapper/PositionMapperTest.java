package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navigator.model.PositionDto;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.enums.Direction;
import com.natwest.submersible.navigation.domain.search.VisitedNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionMapperTest {

    @Test
    void testToDomainFromPositionDto() {
        // Arrange
        PositionDto positionDto = new PositionDto(5, 10);

        // Act
        Position position = PositionMapper.toDomain(positionDto);

        // Assert
        assertNotNull(position);
        assertEquals(5, position.x());
        assertEquals(10, position.y());
    }

    @Test
    void testToDomainFromNullPositionDto() {
        // Act
        Position position = PositionMapper.toDomain((PositionDto) null);

        // Assert
        assertNull(position);
    }

    @Test
    void testToModelFromPosition() {
        // Arrange
        Position position = new Position(3, 7);

        // Act
        PositionDto positionDto = PositionMapper.toModel(position);

        // Assert
        assertNotNull(positionDto);
        assertEquals(3, positionDto.getX());
        assertEquals(7, positionDto.getY());
    }

    @Test
    void testToModelFromNullPosition() {
        // Act
        PositionDto positionDto = PositionMapper.toModel(null);

        // Assert
        assertNull(positionDto);
    }

    @Test
    void testToDomainFromVisitedNode() {
        // Arrange
        Position visitedPosition = new Position(8, 12);
        VisitedNode visitedNode = new VisitedNode(visitedPosition, Direction.EAST);

        // Act
        Position position = PositionMapper.toDomain(visitedNode);

        // Assert
        assertNotNull(position);
        assertEquals(8, position.x());
        assertEquals(12, position.y());
    }

    @Test
    void testToDomainFromNullVisitedNode() {
        // Act
        Position position = PositionMapper.toDomain((VisitedNode) null);

        // Assert
        assertNull(position);
    }
}
