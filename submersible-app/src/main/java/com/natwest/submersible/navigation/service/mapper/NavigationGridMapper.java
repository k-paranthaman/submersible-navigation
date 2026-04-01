package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navigator.model.GridDto;
import com.natwest.submersible.navigation.domain.model.NavigationGrid;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.exception.ErrorCode;
import com.natwest.submersible.navigation.exception.ProbeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * NavigationGridMapper provides mapping utilities between the API data transfer object (GirdDto)
 * and the domain model (NavigationGrid). It converts incoming grid definitions from the API layer
 * into the internal representation used for navigation logic, including mapping obstacle obstacles.
 * <p>
 * Usage:
 * <pre>
 *     NavigationGrid grid = NavigationGridMapper.toDomain(girdDto);
 * </pre>
 */
@Component
@Slf4j
public class NavigationGridMapper {

    /**
     * Maps a GirdDto (API DTO) to a NavigationGrid (domain model).
     * Converts obstacle obstacles and grid dimensions.
     *
     * @param girdDto the grid DTO from the API layer
     * @return NavigationGrid domain object with mapped obstacles and dimensions
     */
    public NavigationGrid toDomain(final GridDto girdDto) {
        log.debug("Mapping GirdDto to NavigationGrid: {}", girdDto);

        if (girdDto == null) {
            log.warn("Received null GirdDto, returning null NavigationGrid");
            throw new ProbeException(ErrorCode.INVALID_INPUT, "Grid definition cannot be null");
        }

        Set<Position> position = girdDto.getObstacles() != null ? girdDto.getObstacles().
                stream().map(PositionMapper::toDomain).collect(Collectors.toSet()) : Set.of();

        NavigationGrid navigationGrid = new NavigationGrid(girdDto.getWidth(), girdDto.getHeight(), position);

        log.debug("Mapped obstacle obstacles: {}", position);
        return navigationGrid;
    }
}
