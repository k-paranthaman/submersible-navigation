package com.natwest.submersible.navigation.domain.validator;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoundaryValidatorTest {

    private BoundaryValidator boundaryValidator;

    @Mock
    private NavigationContext navigationContext;

    @Mock
    private NavigationGrid grid;

    @Mock
    private ProbeState probeState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        boundaryValidator = new BoundaryValidator();
    }

    @Test
    void testValidateWithinBounds() {
        // Arrange
        Position position = new Position(5, 5);
        when(probeState.position()).thenReturn(position);
        when(grid.isWithinBounds(position)).thenReturn(true);
        when(navigationContext.grid()).thenReturn(grid);
        when(navigationContext.probeState()).thenReturn(probeState);

        // Act
        MoveResult result = boundaryValidator.validate(navigationContext);

        // Assert
        assertTrue(result.status());
        assertEquals(probeState, result.probeState());
        assertNull(result.reason());
        verify(grid).isWithinBounds(position);
    }

    @Test
    void testValidateOutOfBounds() {
        // Arrange
        Position position = new Position(10, 10);
        when(probeState.position()).thenReturn(position);
        when(grid.isWithinBounds(position)).thenReturn(false);
        when(navigationContext.grid()).thenReturn(grid);
        when(navigationContext.probeState()).thenReturn(probeState);

        // Act
        MoveResult result = boundaryValidator.validate(navigationContext);

        // Assert
        assertFalse(result.status());
        assertEquals(probeState, result.probeState());
        assertEquals("Probe is out of bounds at position: " + position, result.reason());
        verify(grid).isWithinBounds(position);
    }
}
