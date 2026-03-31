package com.natwest.submersible.navigation.domain.strategy;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Command;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import com.natwest.submersible.navigation.domain.validator.ValidatorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SafeMovementStrategyTest {

    @Mock
    private ValidatorChain validatorChain;

    @Mock
    private NavigationContext navigationContext;

    @Mock
    private ProbeState probeState;

    @InjectMocks
    private SafeMovementStrategy safeMovementStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyForward() {
        // Arrange
        when(navigationContext.probeState()).thenReturn(probeState);
        ProbeState nextState = mock(ProbeState.class);
        when(probeState.forward()).thenReturn(nextState);
        NavigationContext newContext = mock(NavigationContext.class);
        when(navigationContext.withState(nextState)).thenReturn(newContext);
        MoveResult moveResult = mock(MoveResult.class);
        when(validatorChain.validate(newContext)).thenReturn(moveResult);

        // Act
        MoveResult result = safeMovementStrategy.apply(navigationContext, Command.FORWARD);

        // Assert
        assertEquals(moveResult, result);
        verify(probeState).forward();
        verify(validatorChain).validate(newContext);
    }

    @Test
    void testApplyBackward() {
        // Arrange
        when(navigationContext.probeState()).thenReturn(probeState);
        ProbeState nextState = mock(ProbeState.class);
        when(probeState.backward()).thenReturn(nextState);
        NavigationContext newContext = mock(NavigationContext.class);
        when(navigationContext.withState(nextState)).thenReturn(newContext);
        MoveResult moveResult = mock(MoveResult.class);
        when(validatorChain.validate(newContext)).thenReturn(moveResult);

        // Act
        MoveResult result = safeMovementStrategy.apply(navigationContext, Command.BACKWARD);

        // Assert
        assertEquals(moveResult, result);
        verify(probeState).backward();
        verify(validatorChain).validate(newContext);
    }

    @Test
    void testApplyLeft() {
        // Arrange
        when(navigationContext.probeState()).thenReturn(probeState);
        ProbeState nextState = mock(ProbeState.class);
        when(probeState.left()).thenReturn(nextState);
        NavigationContext newContext = mock(NavigationContext.class);
        when(navigationContext.withState(nextState)).thenReturn(newContext);
        MoveResult moveResult = mock(MoveResult.class);
        when(validatorChain.validate(newContext)).thenReturn(moveResult);

        // Act
        MoveResult result = safeMovementStrategy.apply(navigationContext, Command.LEFT);

        // Assert
        assertEquals(moveResult, result);
        verify(probeState).left();
        verify(validatorChain).validate(newContext);
    }

    @Test
    void testApplyRight() {
        // Arrange
        when(navigationContext.probeState()).thenReturn(probeState);
        ProbeState nextState = mock(ProbeState.class);
        when(probeState.right()).thenReturn(nextState);
        NavigationContext newContext = mock(NavigationContext.class);
        when(navigationContext.withState(nextState)).thenReturn(newContext);
        MoveResult moveResult = mock(MoveResult.class);
        when(validatorChain.validate(newContext)).thenReturn(moveResult);

        // Act
        MoveResult result = safeMovementStrategy.apply(navigationContext, Command.RIGHT);

        // Assert
        assertEquals(moveResult, result);
        verify(probeState).right();
        verify(validatorChain).validate(newContext);
    }
}
