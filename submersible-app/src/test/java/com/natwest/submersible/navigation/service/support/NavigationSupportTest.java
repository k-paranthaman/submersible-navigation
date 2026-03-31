package com.natwest.submersible.navigation.service.support;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import com.natwest.submersible.navigation.domain.results.NavigationResult;
import com.natwest.submersible.navigation.domain.strategy.MovementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NavigationSupportTest {

    @Mock
    private MovementStrategy movementStrategy;

    @InjectMocks
    private NavigationSupport navigationSupport;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteCommand_Success() {
        NavigationContext context = mock(NavigationContext.class);
        ProbeState initialState = mock(ProbeState.class);
        Position initialPosition = new Position(0, 0);
        when(context.probeState()).thenReturn(initialState);
        when(initialState.position()).thenReturn(initialPosition);

        ProbeState nextState = mock(ProbeState.class);
        Position nextPosition = new Position(1, 0);
        when(nextState.position()).thenReturn(nextPosition);

        MoveResult moveResult = new MoveResult(true, null, nextState);
        when(movementStrategy.apply(context, Command.FORWARD)).thenReturn(moveResult);

        NavigationContext updatedContext = mock(NavigationContext.class);
        when(updatedContext.probeState()).thenReturn(nextState);
        when(context.withState(nextState)).thenReturn(updatedContext);

        List<Command> commands = List.of(Command.FORWARD);
        NavigationResult result = navigationSupport.executeCommand(context, commands);

        assertTrue(result.status());
        assertEquals(List.of(initialPosition, nextPosition), result.path());
        verify(movementStrategy).apply(context, Command.FORWARD);
    }

    @Test
    void testExecuteCommand_Failure() {
        NavigationContext context = mock(NavigationContext.class);
        ProbeState initialState = mock(ProbeState.class);
        Position initialPosition = new Position(0, 0);
        when(context.probeState()).thenReturn(initialState);
        when(initialState.position()).thenReturn(initialPosition);

        MoveResult moveResult = new MoveResult(false, "Obstacle detected", null);
        when(movementStrategy.apply(context, Command.FORWARD)).thenReturn(moveResult);

        List<Command> commands = List.of(Command.FORWARD);
        NavigationResult result = navigationSupport.executeCommand(context, commands);

        assertFalse(result.status());
        assertEquals("Command FORWARD failed: Obstacle detected", result.reason());
        assertEquals(List.of(initialPosition), result.path());
        verify(movementStrategy).apply(context, Command.FORWARD);
    }
}
