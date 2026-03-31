package com.natwest.submersible.navigation.service.support;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.domain.model.enums.Direction;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import com.natwest.submersible.navigation.domain.results.PathResult;
import com.natwest.submersible.navigation.domain.search.VisitedNode;
import com.natwest.submersible.navigation.domain.strategy.MovementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PathSupportTest {

    @Mock
    private MovementStrategy movementStrategy;

    @InjectMocks
    private PathSupport pathSupport;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindPath_Success() {
        NavigationContext context = mock(NavigationContext.class);
        ProbeState startState = mock(ProbeState.class);
        ProbeState targetState = mock(ProbeState.class);
        Position startPosition = new Position(0, 0);
        Position targetPosition = new Position(1, 0);

        when(context.probeState()).thenReturn(startState);
        when(startState.position()).thenReturn(startPosition);
        when(targetState.position()).thenReturn(targetPosition);
        when(startState.direction()).thenReturn(Direction.NORTH);
        when(targetState.direction()).thenReturn(Direction.EAST);

        MoveResult moveResult = new MoveResult(true, null, targetState);
        when(movementStrategy.apply(any(), any())).thenReturn(moveResult);

        PathResult result = pathSupport.findPath(context, targetState);

        assertTrue(result.status());
        assertEquals(List.of(Command.FORWARD), result.commands());

    }

    @Test
    void testFindPath_Failure() {
        NavigationContext context = mock(NavigationContext.class);
        ProbeState startState = mock(ProbeState.class);
        ProbeState targetState = mock(ProbeState.class);
        Position startPosition = new Position(0, 0);
        Position targetPosition = new Position(1, 0);

        when(context.probeState()).thenReturn(startState);
        when(startState.position()).thenReturn(startPosition);
        when(targetState.position()).thenReturn(targetPosition);
        when(startState.direction()).thenReturn(Direction.NORTH);
        when(targetState.direction()).thenReturn(Direction.EAST);

        MoveResult moveResult = new MoveResult(false, "Obstacle detected", null);
        when(movementStrategy.apply(any(), any())).thenReturn(moveResult);

        PathResult result = pathSupport.findPath(context, targetState);

        assertFalse(result.status());
        assertEquals("Target unreachable", result.reason());

    }
}
