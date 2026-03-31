package com.natwest.submersible.navigation.domain.validator;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObstacleValidatorTest {

    private ObstacleValidator obstacleValidator;

    @Mock
    private NavigationContext navigationContext;

    @Mock
    private NavigationGrid grid;

    @Mock
    private ProbeState probeState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        obstacleValidator = new ObstacleValidator();
    }

    @Test
    void testValidateNoObstacle() {
        // Arrange
        Position position = new Position(5, 5);
        when(probeState.position()).thenReturn(position);
        when(grid.isObstacle(position)).thenReturn(false);
        when(navigationContext.grid()).thenReturn(grid);
        when(navigationContext.probeState()).thenReturn(probeState);

        // Act
        MoveResult result = obstacleValidator.validate(navigationContext);

        // Assert
        assertTrue(result.status());
        assertEquals(probeState, result.probeState());
        assertNull(result.reason());
        verify(grid).isObstacle(position);
    }

    @Test
    void testValidateWithObstacle() {
        // Arrange
        Position position = new Position(10, 10);
        when(probeState.position()).thenReturn(position);
        when(grid.isObstacle(position)).thenReturn(true);
        when(navigationContext.grid()).thenReturn(grid);
        when(navigationContext.probeState()).thenReturn(probeState);

        // Act
        MoveResult result = obstacleValidator.validate(navigationContext);

        // Assert
        assertFalse(result.status());
        assertEquals(probeState, result.probeState());
        assertEquals("Obstacle detected at position: " + position, result.reason());
        verify(grid).isObstacle(position);
    }
}
