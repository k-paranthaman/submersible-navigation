package com.natwest.submersible.navigation.api;

import com.natwest.submersible.navidator.model.NavigationRequest;
import com.natwest.submersible.navidator.model.NavigationResponse;
import com.natwest.submersible.navigation.service.NavigationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class NavigationControllerTest {

    @InjectMocks
    private NavigationController navigationController;

    @Mock
    private NavigationService navigationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteNavigation_Success() {
        // Arrange
        NavigationRequest navigationRequest = new NavigationRequest();
        NavigationResponse navigationResponse = new NavigationResponse();
        when(navigationService.executeNavigation(navigationRequest)).thenReturn(navigationResponse);

        // Act
        ResponseEntity<NavigationResponse> response = navigationController.executeNavigation(navigationRequest);

        // Assert
        assertEquals(ResponseEntity.ok(navigationResponse), response);
    }
}
