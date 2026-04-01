package com.natwest.submersible.navigation.api;

import com.natwest.submersible.navigator.api.NavigationApi;
import com.natwest.submersible.navigator.model.NavigationRequest;
import com.natwest.submersible.navigator.model.NavigationResponse;
import com.natwest.submersible.navigation.service.NavigationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * NavigationController exposes REST endpoints for navigation operations.
 * It implements the NavigationApi interface and delegates business logic to NavigationService.
 * Handles incoming navigation requests, logs activity, and returns navigation responses.
 * <p>
 * Usage:
 * <pre>
 *     POST /api/v1/navigattion/execute
 * </pre>
 * The controller validates requests and returns the result of navigation execution.
 * 
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class NavigationController implements NavigationApi {

    private final NavigationService navigationService;


    /**
     * Handles the execution of navigation commands for a probe.
     * Accepts a validated NavigationRequest, delegates to NavigationService, and returns the result.
     *
     * @param navigationRequest the navigation request payload
     * @return ResponseEntity containing the NavigationResponse
     */
    @Override
    public ResponseEntity<NavigationResponse> executeNavigation(@Valid @RequestBody NavigationRequest navigationRequest) {
        log.debug("Received navigation request: {}", navigationRequest);
        return ResponseEntity.ok(navigationService.executeNavigation(navigationRequest));
    }
}
