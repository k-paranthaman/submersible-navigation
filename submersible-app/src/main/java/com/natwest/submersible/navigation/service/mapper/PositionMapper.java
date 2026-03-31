package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navidator.model.PositionDto;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.search.VisitedNode;
import lombok.extern.slf4j.Slf4j;

/**
 * PositionMapper provides mapping utilities between the API data transfer object (PositionDto)
 * and the domain model (Position). It converts between the external and internal representations
 * of a probe's position for use in navigation logic and API communication.
 * <p>
 * Usage:
 * <pre>
 *     Position position = PositionMapper.toDomain(positionDto);
 *     PositionDto dto = PositionMapper.toModel(position);
 * </pre>
 */
@Slf4j
public class PositionMapper {

    /**
     * Maps a PositionDto (API DTO) to a Position (domain model).
     *
     * @param positionDto the position DTO from the API layer
     * @return Position domain object or null if input is null
     */
    public static Position toDomain(PositionDto positionDto) {
        if (positionDto == null) {
            log.debug("Received null PositionDto, returning null Position");
            return null;
        }
        return new Position(positionDto.getX(), positionDto.getY());
    }

    /**
     * Maps a Position (domain model) to a PositionDto (API DTO).
     *
     * @param position the domain Position object
     * @return PositionDto for API layer or null if input is null
     */
    public static PositionDto toModel(Position position) {
        if (position == null) {
            log.debug("Received null Position, returning null PositionDto");
            return null;
        }
        return new PositionDto(position.x(), position.y());
    }


    public static Position toDomain(VisitedNode visitedNode) {
        if (visitedNode == null) {
            log.debug("Received null visitedNode, returning null Position");
            return null;
        }
        return new Position(visitedNode.position().x(), visitedNode.position().y());
    }
}
