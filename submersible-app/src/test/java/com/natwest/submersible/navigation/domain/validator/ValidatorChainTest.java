package com.natwest.submersible.navigation.domain.validator;

import com.natwest.submersible.navigation.domain.context.NavigationContext;
import com.natwest.submersible.navigation.domain.results.MoveResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidatorChainTest {

    private ValidatorChain validatorChain;

    @Mock
    private Validator boundaryValidator;

    @Mock
    private Validator obstacleValidator;

    @Mock
    private NavigationContext navigationContext;

    @Mock
    private MoveResult successResult;

    @Mock
    private MoveResult failureResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validatorChain = new ValidatorChain(boundaryValidator, obstacleValidator);

        when(successResult.status()).thenReturn(true);
        when(failureResult.status()).thenReturn(false);
    }

    @Test
    void testValidateAllPass() {
        // Arrange
        when(boundaryValidator.validate(navigationContext)).thenReturn(successResult);
        when(obstacleValidator.validate(navigationContext)).thenReturn(successResult);

        // Act
        MoveResult result = validatorChain.validate(navigationContext);

        // Assert
        assertTrue(result.status());
        verify(boundaryValidator).validate(navigationContext);
        verify(obstacleValidator).validate(navigationContext);
    }

    @Test
    void testValidateBoundaryFails() {
        // Arrange
        when(boundaryValidator.validate(navigationContext)).thenReturn(failureResult);

        // Act
        MoveResult result = validatorChain.validate(navigationContext);

        // Assert
        assertFalse(result.status());
        verify(boundaryValidator).validate(navigationContext);
        verify(obstacleValidator, never()).validate(navigationContext);
    }

    @Test
    void testValidateObstacleFails() {
        // Arrange
        when(boundaryValidator.validate(navigationContext)).thenReturn(successResult);
        when(obstacleValidator.validate(navigationContext)).thenReturn(failureResult);

        // Act
        MoveResult result = validatorChain.validate(navigationContext);

        // Assert
        assertFalse(result.status());
        verify(boundaryValidator).validate(navigationContext);
        verify(obstacleValidator).validate(navigationContext);
    }
}
