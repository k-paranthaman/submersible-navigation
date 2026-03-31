package com.natwest.submersible.navigation.service;

import com.natwest.submersible.navidator.model.*;
import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import com.natwest.submersible.navigation.domain.results.NavigationResult;
import com.natwest.submersible.navigation.domain.validator.ValidatorChain;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import com.natwest.submersible.navigation.service.mapper.NavigationGridMapper;
import com.natwest.submersible.navigation.service.mapper.ProbeStateMapper;
import com.natwest.submersible.navigation.service.support.NavigationSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NavigationServiceTest {

    @Mock
    private NavigationSupport navigationSupport;

    @Mock
    private ValidatorChain validatorChain;

    @Mock
    private NavigationGridMapper navigationGridMapper;

    @Mock
    private ProbeStateMapper probeStateMapper;

    @InjectMocks
    private NavigationService navigationService;

    // Wrap the openMocks call in a try-catch block to handle exceptions
    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this).close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize mocks", e);
        }
    }

    @Test
    void testExecuteNavigation_Success() {
        NavigationRequest request = mock(NavigationRequest.class);
        NavigationGrid grid = mock(NavigationGrid.class);
        ProbeState probeState = mock(ProbeState.class);
        NavigationContext context = new NavigationContext(grid, probeState);
        NavigationResult result = mock(NavigationResult.class);

        GridDto gridDto = mock(GridDto.class);
        when(gridDto.getObstacles()).thenReturn(List.of());
        when(request.getGrid()).thenReturn(gridDto);
        when(request.getProbeState()).thenReturn(mock(StateDto.class));
        when(request.getCommands()).thenReturn("F");

        when(navigationGridMapper.toDomain(gridDto)).thenReturn(grid);
        when(probeStateMapper.toDomain(any())).thenReturn(probeState);

        MoveResult validationResult = new MoveResult(true, null, probeState);
        when(validatorChain.validate(context)).thenReturn(validationResult);
        when(navigationSupport.executeCommand(context, List.of(Command.FORWARD))).thenReturn(result);

        when(result.status()).thenReturn(true);
        when(result.path()).thenReturn(List.of());
        when(result.probeState()).thenReturn(probeState);

        NavigationResponse response = navigationService.executeNavigation(request);

        assertNotNull(response);
        assertEquals(Status.SUCCESS, response.getStatus());
        assertNotNull(response.getPath());
        verify(navigationSupport).executeCommand(context, List.of(Command.FORWARD));
    }

    @Test
    void testExecuteNavigation_InvalidPosition() {
        NavigationRequest request = mock(NavigationRequest.class);
        NavigationGrid grid = mock(NavigationGrid.class);
        ProbeState probeState = mock(ProbeState.class);
        NavigationContext context = mock(NavigationContext.class);

        GridDto girdDto = mock(GridDto.class);
        when(girdDto.getObstacles()).thenReturn(List.of());
        when(request.getGrid()).thenReturn(girdDto);
        when(request.getProbeState()).thenReturn(mock(StateDto.class));
        when(request.getCommands()).thenReturn("F");

        when(navigationGridMapper.toDomain(girdDto)).thenReturn(grid);
        when(probeStateMapper.toDomain(any())).thenReturn(probeState);


        when(context.probeState()).thenReturn(probeState);

        MoveResult validationResult = new MoveResult(false, "Invalid position", null);
        when(validatorChain.validate(any())).thenReturn(validationResult);

        ProbeException exception = assertThrows(ProbeException.class, () -> navigationService.executeNavigation(request));
        assertEquals(ErrorCode.INVALID_POSITION, exception.getErrorCode());
    }
}
