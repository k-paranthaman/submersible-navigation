package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navidator.model.GirdDto;
import com.natwest.submersible.navidator.model.PositionDto;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NavigationGridMapperTest {

    @Test
    void testToDomainWithObstacles() {
        // Arrange
        GirdDto girdDto = mock(GirdDto.class);
        when(girdDto.getWidth()).thenReturn(10);
        when(girdDto.getHeight()).thenReturn(15);
        when(girdDto.getObstacles()).thenReturn(List.of(new PositionDto(1,1)));

        // Act
        NavigationGrid navigationGrid = NavigationGridMapper.toDomain(girdDto);

        // Assert
        assertEquals(10, navigationGrid.width());
        assertEquals(15, navigationGrid.height());
        assertEquals(1, navigationGrid.obstracles().size());
    }

    @Test
    void testToDomainWithNoObstacles() {
        // Arrange
        GirdDto girdDto = mock(GirdDto.class);
        when(girdDto.getWidth()).thenReturn(5);
        when(girdDto.getHeight()).thenReturn(5);
        when(girdDto.getObstacles()).thenReturn(null);

        // Act
        NavigationGrid navigationGrid = NavigationGridMapper.toDomain(girdDto);

        // Assert
        assertEquals(5, navigationGrid.width());
        assertEquals(5, navigationGrid.height());
        assertTrue(navigationGrid.obstracles().isEmpty());
    }
}
