package com.natwest.submersible.navigation.domain.model;

import com.natwest.submersible.navigation.exception.ProbeException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NavigationGridTest {

    @Test
    void construct_valid_grid_create_successfully() {
        NavigationGrid grid = new NavigationGrid(5, 5, Set.of(new Position(1, 1)));
        assertEquals(5, grid.width());
        assertEquals(5, grid.height());
        assertTrue(grid.obstacles().contains(new Position(1, 1)));
    }

    @Test
    void construct_null_obstacles__create_empty_obstacle(){
        NavigationGrid grid = new NavigationGrid(5, 5, null);
        assertEquals(5, grid.width());
        assertEquals(5, grid.height());
        assertTrue(grid.obstacles().isEmpty());
        assertNotNull(grid.obstacles());
    }

    @Test
    void construct_max_size_grid_throw_exception() {
        assertThrows(ProbeException.class, () -> new NavigationGrid(1, Integer.MAX_VALUE, null));
        assertThrows(ProbeException.class, () -> new NavigationGrid(Integer.MAX_VALUE, 1, null));

    }

    @Test
    void is_within_bounds_return_true() {
        NavigationGrid grid = new NavigationGrid(5, 5, Set.of());
        assertTrue(grid.isWithinBounds(new Position(0, 0)));
        assertTrue(grid.isWithinBounds(new Position(4, 4)));
    }

    @Test
    void is_within_bounds_return_false(){
        NavigationGrid grid = new NavigationGrid(5, 5, Set.of());
        assertFalse(grid.isWithinBounds(new Position(6, 0)));
        assertFalse(grid.isWithinBounds(new Position(0, 6)));
        assertFalse(grid.isWithinBounds(new Position(-1, 4)));
        assertFalse(grid.isWithinBounds(new Position(4, -1)));

    }

    @Test
    void is_obstacle_present(){
        NavigationGrid grid = new NavigationGrid(5, 5, Set.of(new Position(1, 1)));

        assertFalse(grid.isObstacle(new Position(0, 0)));
        assertTrue(grid.isObstacle(new Position(1, 1)));
        assertThrows(NullPointerException.class, () -> grid.isObstacle(null));

    }

}
