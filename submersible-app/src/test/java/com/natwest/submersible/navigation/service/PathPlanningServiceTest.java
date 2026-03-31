package com.natwest.submersible.navigation.service;

import com.natwest.submersible.navidator.model.PathPlanningRequest;
import com.natwest.submersible.navidator.model.PathPlanningResponse;
import com.natwest.submersible.navidator.model.Status;
import com.natwest.submersible.navidator.model.GridDto;
import com.natwest.submersible.navidator.model.StateDto;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.domain.results.PathResult;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.service.support.PathSupport;
import com.natwest.submersible.navigation.service.mapper.NavigationGridMapper;
import com.natwest.submersible.navigation.service.mapper.ProbeStateMapper;
import org.apache.tomcat.util.json.ParseException;
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

    @Mock
    private NavigationGridMapper navigationGridMapper;

    @Mock
    private ProbeStateMapper probeStateMapper;

    @InjectMocks
    private PathPlanningService pathPlanningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlanPath_Success() {
        PathPlanningRequest request = mock(PathPlanningRequest.class);
        GridDto gridDto = mock(GridDto.class);
        StateDto currentStateDto = mock(StateDto.class);
        StateDto targetStateDto = mock(StateDto.class);
        NavigationGrid navigationGrid = mock(NavigationGrid.class);
        ProbeState currentState = mock(ProbeState.class);
        ProbeState targetState = mock(ProbeState.class);
        PathResult pathResult = mock(PathResult.class);

        // Mocking request data
        when(request.getGrid()).thenReturn(gridDto);
        when(request.getCurrentState()).thenReturn(currentStateDto);
        when(request.getTargetState()).thenReturn(targetStateDto);

        // Mocking mappers
        when(navigationGridMapper.toDomain(gridDto)).thenReturn(navigationGrid);
        when(probeStateMapper.toDomain(currentStateDto)).thenReturn(currentState);
        when(probeStateMapper.toDomain(targetStateDto)).thenReturn(targetState);

        // Mocking path support
        when(pathSupport.findPath(any(), any())).thenReturn(pathResult);

        // Mocking path result
        when(pathResult.commands()).thenReturn(List.of(Command.FORWARD));
        when(pathResult.status()).thenReturn(true);
        when(pathResult.path()).thenReturn(List.of(mock(Position.class), mock(Position.class)));

        // Execute the service method
        PathPlanningResponse response = pathPlanningService.planPath(request);

        // Assertions
        assertNotNull(response);
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals(2, response.getPath().size()); // Verify path size
        assertEquals("F", response.getCommands());

    }

    @Test
    void testPlanPath_Failure() {
        PathPlanningRequest request = mock(PathPlanningRequest.class);
        NavigationGrid navigationGrid = mock(NavigationGrid.class);
        ProbeState currentState = mock(ProbeState.class);
        ProbeState targetState = mock(ProbeState.class);
        PathResult pathResult = mock(PathResult.class);

        when(request.getGrid()).thenReturn(mock(GridDto.class));
        when(request.getCurrentState()).thenReturn(mock(StateDto.class));
        when(request.getTargetState()).thenReturn(mock(StateDto.class));

        when(navigationGridMapper.toDomain(any())).thenReturn(navigationGrid);
        when(probeStateMapper.toDomain(any())).thenReturn(currentState).thenReturn(targetState);

        when(pathResult.commands()).thenReturn(List.of());
        when(pathResult.status()).thenReturn(false);
        when(pathResult.path()).thenReturn(List.of());
        when(pathSupport.findPath(any(), any())).thenReturn(pathResult);

        PathPlanningResponse response = pathPlanningService.planPath(request);

        assertNotNull(response);
        assertEquals(Status.FAILURE, response.getStatus());
        assertNotNull(response.getPath());
        assertNotNull(response.getCommands());

        verify(pathSupport).findPath(any(), eq(targetState));
    }
}
