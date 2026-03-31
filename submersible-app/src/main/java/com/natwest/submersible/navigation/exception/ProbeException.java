package com.natwest.submersible.navigation.exception;

import lombok.Getter;
import org.springframework.http.ProblemDetail;

/**
 * ProbeException is a custom runtime exception for handling probe-related errors in the navigation domain.
 * It encapsulates an ErrorCode and provides utility methods for converting the exception to a standardized ProblemDetail
 * for API error responses. Use this exception to signal business logic errors with rich error information.
 * <p>
 * Usage:
 * <pre>
 *     throw new ProbeException(ErrorCode.INVALID_COMMAND);
 *     throw new ProbeException(ErrorCode.OUT_OF_BOUND, "Custom message");
 * </pre>
 */
@Getter
public class ProbeException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Constructs a ProbeException with the given error code, using the error code's default message.
     *
     * @param errorCode the error code representing the error type
     */
    public ProbeException(final ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Constructs a ProbeException with the given error code and a custom message.
     *
     * @param errorCode the error code representing the error type
     * @param message   the custom error message
     */
    public ProbeException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Converts this exception to a Spring ProblemDetail object for standardized API error responses.
     * Sets the HTTP status, title, detail, and error code properties.
     *
     * @return ProblemDetail representing this exception for API responses
     */
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getHttpStatus());
        problemDetail.setTitle("Probe Error");
        problemDetail.setDetail(getMessage());
        problemDetail.setProperty("code", errorCode.getCode());
        return problemDetail;
    }


}
