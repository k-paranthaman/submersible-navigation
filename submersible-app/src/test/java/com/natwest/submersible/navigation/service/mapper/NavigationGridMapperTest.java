package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navidator.model.GridDto;
import com.natwest.submersible.navidator.model.PositionDto;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NavigationGridMapperTest {

    private final NavigationGridMapper navigationGridMapper = new NavigationGridMapper();

    @Test
    void testToDomainWithObstacles() {
        // Arrange
        GridDto girdDto = mock(GridDto.class);
        when(girdDto.getWidth()).thenReturn(10);
        when(girdDto.getHeight()).thenReturn(15);
        when(girdDto.getObstacles()).thenReturn(List.of(new PositionDto(1,1)));

        // Act
        NavigationGrid navigationGrid = navigationGridMapper.toDomain(girdDto);

        // Assert
        assertEquals(10, navigationGrid.width());
        assertEquals(15, navigationGrid.height());
        assertEquals(1, navigationGrid.obstacles().size());
    }

    @Test
    void testToDomainWithNoObstacles() {
        // Arrange
        GridDto girdDto = mock(GridDto.class);
        when(girdDto.getWidth()).thenReturn(5);
        when(girdDto.getHeight()).thenReturn(5);
        when(girdDto.getObstacles()).thenReturn(null);

        // Act
        NavigationGrid navigationGrid = navigationGridMapper.toDomain(girdDto);

        // Assert
        assertEquals(5, navigationGrid.width());
        assertEquals(5, navigationGrid.height());
        assertTrue(navigationGrid.obstacles().isEmpty());
    }
}
