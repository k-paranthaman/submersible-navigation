package com.natwest.submersible.navigation.api;

import com.natwest.submersible.navidator.model.PathPlanningRequest;
import com.natwest.submersible.navidator.model.PathPlanningResponse;
import com.natwest.submersible.navigation.service.PathPlanningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PathPlanningControllerTest {

    @InjectMocks
    private PathPlanningController pathPlanningController;

    @Mock
    private PathPlanningService pathPlanningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlanPath_Success() {
        // Arrange
        PathPlanningRequest pathPlanningRequest = new PathPlanningRequest();
        PathPlanningResponse pathPlanningResponse = new PathPlanningResponse();
        when(pathPlanningService.planPath(pathPlanningRequest)).thenReturn(pathPlanningResponse);

        // Act
        ResponseEntity<PathPlanningResponse> response = pathPlanningController.planPath(pathPlanningRequest);

        // Assert
        assertEquals(ResponseEntity.ok(pathPlanningResponse), response);
    }
}
