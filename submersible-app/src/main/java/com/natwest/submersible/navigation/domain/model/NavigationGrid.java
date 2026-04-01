package com.natwest.submersible.navigation.domain.model;

import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * NavigationGrid represents the navigation area for the probe, including its dimensions and obstacle obstacles.
 * It validates grid size on creation and provides methods to check if a position is within bounds or is an obstacle.
 * All actions are logged for traceability. This record is immutable.
 * <p>
 * Usage:
 * <pre>
 *     NavigationGrid grid = new NavigationGrid(width, height, obstacles);
 *     boolean inBounds = grid.isWithinBounds(position);
 *     boolean isObstacle = grid.isObstacle(position);
 * </pre>
 */
@Slf4j
public record NavigationGrid(int width, int height, Set<Position> obstacles) {

    /**
     * Constructs a NavigationGrid with the given dimensions and obstacle obstacles.
     * Validates that the grid size is within allowed limits.
     *
     * @param width      the width of the grid (must be 1..1000)
     * @param height     the height of the grid (must be 1..1000)
     * @param obstacles  the set of obstacle obstacles
     * @throws IllegalArgumentException if dimensions are out of bounds
     */
    public NavigationGrid(int width, int height, Set<Position> obstacles) {
        int MAX_SIZE = 1000;

        if (width <= 0 || height <= 0 || width > MAX_SIZE || height > MAX_SIZE) {
            log.info("Invalid grid dimensions: width={}, height={}", width, height);
            throw new ProbeException(ErrorCode.INVALID_INPUT, "Grid dimensions must be between 1 and " + MAX_SIZE);
        }
        this.width = width;
        this.height = height;
        if(obstacles == null){
            log.info("No obstacles provided, initializing with empty set");
            this.obstacles = Set.of();
        } else {
            this.obstacles = obstacles;
        }

        log.info("Created NavigationGrid with dimensions {}x{} and {} obstacles", width, height, this.obstacles.size());
    }

    /**
     * Checks if the given position is within the grid bounds.
     *
     * @param position the position to check
     * @return true if the position is within bounds, false otherwise
     */
    public boolean isWithinBounds(final Position position) {

        boolean result = position.x() >= 0 && position.x() < width && position.y() >= 0 && position.y() < height;
        log.debug("Checking if position {} is within bounds: {}", position, result);

        if(result){
            log.info("Position {} is out of bounds for grid dimensions {}x{}", position, width, height);
        }
        return result;
    }

    /**
     * Checks if the given position is an obstacle in the grid.
     *
     * @param position the position to check
     * @return true if the position is an obstacle, false otherwise
     */
    public boolean isObstacle(final Position position){
        boolean result = obstacles.contains(position);
        log.debug("Checking if position {} is an obstacle: {}", position, result);
        if(result) {
            log.info("Position {} is an obstacle", position);
        }
        return result;
    }
}
