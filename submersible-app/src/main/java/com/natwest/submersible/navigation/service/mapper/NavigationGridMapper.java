package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navidator.model.GirdDto;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.Position;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * NavigationGridMapper provides mapping utilities between the API data transfer object (GirdDto)
 * and the domain model (NavigationGrid). It converts incoming grid definitions from the API layer
 * into the internal representation used for navigation logic, including mapping obstacle obstracles.
 * <p>
 * Usage:
 * <pre>
 *     NavigationGrid grid = NavigationGridMapper.toDomain(girdDto);
 * </pre>
 */
@Slf4j
public class NavigationGridMapper {

    /**
     * Maps a GirdDto (API DTO) to a NavigationGrid (domain model).
     * Converts obstacle obstracles and grid dimensions.
     *
     * @param girdDto the grid DTO from the API layer
     * @return NavigationGrid domain object with mapped obstacles and dimensions
     */
    public static NavigationGrid toDomain(final GirdDto girdDto) {
        log.debug("Mapping GirdDto to NavigationGrid: {}", girdDto);
        Set<Position> position = girdDto.getObstacles().
                stream().map(PositionMapper::toDomain).collect(Collectors.toSet());;
        NavigationGrid navigationGrid = new NavigationGrid(girdDto.getWidth(), girdDto.getHeight(), position);

        log.debug("Mapped obstacle obstracles: {}", position);
        return navigationGrid;
    }
}
