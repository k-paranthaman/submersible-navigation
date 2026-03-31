package com.natwest.submersible.navigation.advisors;

import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

/**
 * Global exception handler for the navigation application.
 * <p>
 * Handles and translates exceptions thrown by controllers and services into standardized HTTP error responses using {@link ProblemDetail}.
 * <ul>
 *   <li>Handles {@link ProbeException} and converts it to a problem detail with domain-specific error codes.</li>
 *   <li>Handles {@link MethodArgumentNotValidException} for validation errors, providing detailed field-level error information.</li>
 *   <li>Handles all other uncaught exceptions as internal server errors.</li>
 * </ul>
 * This class centralizes error handling logic to ensure consistent API error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles domain-specific {@link ProbeException} and converts it to a standardized problem detail response.
     *
     * @param ex the thrown ProbeException
     * @return a {@link ProblemDetail} containing error details and code
     */
    @ExceptionHandler(ProbeException.class)
    public ProblemDetail handleProbeExceptions(ProbeException ex) {
       return ex.toProblemDetail();
    }

    /**
     * Handles validation errors for method arguments (e.g., invalid request bodies).
     *
     * @param ex the thrown MethodArgumentNotValidException
     * @return a {@link ProblemDetail} with HTTP 400 status, error code, and field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(400);
        problem.setTitle("Bad Request");
        problem.setDetail(ex.getMessage());

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .toList();

        problem.setProperty("code", ErrorCode.INVALID_INPUT.getCode());
        problem.setProperty("errors", errors);
        return problem;
    }

    /**
     * Handles all uncaught exceptions and returns a generic internal server error response.
     *
     * @param ex the thrown Exception
     * @return a {@link ProblemDetail} with HTTP 500 status and internal error code
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllExceptions(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatus(500);
        problem.setTitle("Internal Server Error");
        problem.setDetail(ex.getMessage());
        problem.setProperty("code", ErrorCode.INTERNAL_ERROR.getCode());
        return problem;
    }
}
