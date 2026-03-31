package com.natwest.submersible.navigation.exception;

import lombok.Getter;

/**
 * Enumeration of error codes used throughout the navigation application.
 * <p>
 * Each error code represents a specific error scenario that can occur during navigation operations, such as invalid commands,
 * out-of-bounds moves, obstacle collisions, invalid directions, invalid input, and internal server errors.
 * <p>
 * Each enum constant includes:
 * <ul>
 *   <li><b>code</b>: A unique string identifier for the error (e.g., "E001").</li>
 *   <li><b>message</b>: A human-readable description of the error.</li>
 *   <li><b>httpStatus</b>: The HTTP status code associated with the error.</li>
 * </ul>
 * <p>
 * This enum is used by exception handlers and domain logic to provide consistent error responses to API clients.
 */
@Getter
public enum ErrorCode {

    /**
     * Invalid command error.
     * <p>
     * This error occurs when a command is not recognized or is malformed.
     */
    INVALID_COMMAND("E001", "Invalid command", 400),

    /**
     * Out of bound error.
     * <p>
     * This error occurs when a move attempts to go outside the defined grid boundaries.
     */
    OUT_OF_BOUND("E002", "Move goes outside grid", 400),

    /**
     * Obstacle hit error.
     * <p>
     * This error occurs when a move encounters an obstacle in the grid.
     */
    OBSTACLE_HIT("E003", "Obstacle encountered", 409),

    /**
     * Invalid direction error.
     * <p>
     * This error occurs when a specified direction is not valid.
     */
    INVALID_DIRECTION("E004", "Invalid direction", 400),

    /**
     * Invalid input error.
     * <p>
     * This error occurs when the request input does not conform to the expected format or values.
     */
    INVALID_INPUT("E005", "Invalid request input", 400),

    INVALID_POSITION("E006", "Invalid position input", 400),

    INVALID_TARGET("E007", "Invalid target position", 400),

    /**
     * Internal server error.
     * <p>
     * This error indicates an unexpected condition was encountered within the server.
     */
    INTERNAL_ERROR("E999", "Internal Server error", 500);


    /**
     * Unique string identifier for the error code (e.g., "E001").
     */
    private final String code;
    /**
     * Human-readable description of the error.
     */
    private final String message;
    /**
     * HTTP status code associated with the error.
     */
    private final int httpStatus;

    /**
     * Constructs an ErrorCode enum constant.
     *
     * @param code unique error code string
     * @param message error description
     * @param httpStatus associated HTTP status code
     */
    ErrorCode(String code, String message, int httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
