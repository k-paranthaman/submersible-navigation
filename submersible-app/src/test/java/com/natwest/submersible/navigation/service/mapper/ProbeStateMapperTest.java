package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navidator.model.DirectionDto;
import com.natwest.submersible.navidator.model.PositionDto;
import com.natwest.submersible.navidator.model.StateDto;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProbeStateMapperTest {

    @Test
    void testToDomainWithValidStateDto() {
        // Arrange
        PositionDto positionDto = new PositionDto(5, 10);
        DirectionDto directionDto = DirectionDto.NORTH;
        StateDto stateDto = new StateDto(positionDto, directionDto);

        // Act
        ProbeState probeState = ProbeStateMapper.toDomain(stateDto);

        // Assert
        assertNotNull(probeState);
        assertEquals(5, probeState.position().x());
        assertEquals(10, probeState.position().y());
        assertEquals(Direction.NORTH, probeState.direction());
    }

    @Test
    void testToDomainWithNullStateDto() {
        // Act
        ProbeState probeState = ProbeStateMapper.toDomain(null);

        // Assert
        assertNull(probeState);
    }

    @Test
    void testToModelWithValidProbeState() {
        // Arrange
        Position position = new Position(3, 7);
        Direction direction = Direction.EAST;
        ProbeState probeState = new ProbeState(position, direction);

        // Act
        StateDto stateDto = ProbeStateMapper.toModel(probeState);

        // Assert
        assertNotNull(stateDto);
        assertEquals(3, stateDto.getPosition().getX());
        assertEquals(7, stateDto.getPosition().getY());
        assertEquals(DirectionDto.EAST, stateDto.getDirection());
    }

    @Test
    void testToModelWithNullProbeState() {
        // Act
        StateDto stateDto = ProbeStateMapper.toModel(null);

        // Assert
        assertNull(stateDto);
    }
}
