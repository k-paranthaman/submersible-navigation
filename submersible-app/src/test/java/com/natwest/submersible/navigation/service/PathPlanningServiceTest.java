package com.natwest.submersible.navigation.service;

import com.natwest.submersible.navidator.model.PathPlanningRequest;
import com.natwest.submersible.navidator.model.PathPlanningResponse;
import com.natwest.submersible.navidator.model.PositionDto;
import com.natwest.submersible.navidator.model.Status;
import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.results.PathResult;
import com.natwest.submersible.navigation.service.support.PathSupport;
import com.natwest.submersible.navigation.service.mapper.NavigationGridMapper;
import com.natwest.submersible.navigation.service.mapper.PositionMapper;
import com.natwest.submersible.navigation.service.mapper.ProbeStateMapper;
import com.natwest.submersible.navigation.service.parser.CommandParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PathPlanningServiceTest {

    @Mock
    private PathSupport pathSupport;

    @InjectMocks
    private PathPlanningService pathPlanningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlanPath_Success() {
        PathPlanningRequest request = mock(PathPlanningRequest.class);
        NavigationGrid navigationGrid = mock(NavigationGrid.class);
        ProbeState currentState = mock(ProbeState.class);
        ProbeState targetState = mock(ProbeState.class);
        NavigationContext context = mock(NavigationContext.class);
        PathResult pathResult = mock(PathResult.class);

        when(request.getGrid()).thenReturn(null);
        when(request.getCurrentState()).thenReturn(null);
        when(request.getTargetState()).thenReturn(null);

        when(NavigationGridMapper.toDomain(null)).thenReturn(navigationGrid);
        when(ProbeStateMapper.toDomain(null)).thenReturn(currentState).thenReturn(targetState);
        when(pathSupport.findPath(any(NavigationContext.class), eq(targetState))).thenReturn(pathResult);

        when(pathResult.commands()).thenReturn(List.of());
        when(pathResult.status()).thenReturn(true);
        when(pathResult.path()).thenReturn(List.of());

        PathPlanningResponse response = pathPlanningService.planPath(request);

        assertNotNull(response);
        assertEquals(Status.SUCCESS, response.getStatus());
        assertNotNull(response.getPath());
        assertNotNull(response.getCommands());

        verify(pathSupport).findPath(any(NavigationContext.class), eq(targetState));
    }

    @Test
    void testPlanPath_Failure() {
        PathPlanningRequest request = mock(PathPlanningRequest.class);
        NavigationGrid navigationGrid = mock(NavigationGrid.class);
        ProbeState currentState = mock(ProbeState.class);
        ProbeState targetState = mock(ProbeState.class);
        NavigationContext context = mock(NavigationContext.class);
        PathResult pathResult = mock(PathResult.class);

        when(request.getGrid()).thenReturn(null);
        when(request.getCurrentState()).thenReturn(null);
        when(request.getTargetState()).thenReturn(null);

        when(NavigationGridMapper.toDomain(null)).thenReturn(navigationGrid);
        when(ProbeStateMapper.toDomain(null)).thenReturn(currentState).thenReturn(targetState);
        when(pathSupport.findPath(any(NavigationContext.class), eq(targetState))).thenReturn(pathResult);

        when(pathResult.commands()).thenReturn(List.of());
        when(pathResult.status()).thenReturn(false);
        when(pathResult.path()).thenReturn(List.of());

        PathPlanningResponse response = pathPlanningService.planPath(request);

        assertNotNull(response);
        assertEquals(Status.FAILURE, response.getStatus());
        assertNotNull(response.getPath());
        assertNotNull(response.getCommands());

        verify(pathSupport).findPath(any(NavigationContext.class), eq(targetState));
    }
}
