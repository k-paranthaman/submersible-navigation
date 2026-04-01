package com.natwest.submersible.navigation.service.mapper;

import com.natwest.submersible.navigator.model.DirectionDto;
import com.natwest.submersible.navigator.model.PositionDto;
import com.natwest.submersible.navigator.model.StateDto;
import com.natwest.submersible.navigation.domain.model.Position;
import com.natwest.submersible.navigation.domain.model.ProbeState;
import com.natwest.submersible.navigation.domain.model.enums.Direction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ProbeStateMapper provides mapping utilities between the API data transfer object (StateDto)
 * and the domain model (ProbeState). It converts between the external and internal representations
 * of a probe's state, including position and direction, for use in navigation logic and API communication.
 * <p>
 * Usage:
 * <pre>
 *     ProbeState state = ProbeStateMapper.toDomain(stateDto);
 *     StateDto dto = ProbeStateMapper.toModel(state);
 * </pre>
 */
@Slf4j
@Component
public class ProbeStateMapper {

    /**
     * Maps a StateDto (API DTO) to a ProbeState (domain model).
     *
     * @param stateDto the state DTO from the API layer
     * @return ProbeState domain object or null if input is null
     */
    public ProbeState toDomain(final StateDto stateDto) {

        if (stateDto == null) {
            log.info("Received null StateDto, returning null ProbeState");
            return null;
        }

        return new ProbeState(
                new Position(stateDto.getPosition().getX(), stateDto.getPosition().getY()),
                Direction.valueOf(stateDto.getDirection().name())
        );
    }

    /**
     * Maps a ProbeState (domain model) to a StateDto (API DTO).
     *
     * @param state the domain ProbeState object
     * @return StateDto for API layer or null if input is null
     */
    public StateDto toModel(final ProbeState state) {
        if (state == null) {
            log.info("Received null ProbeState, returning null StateDto");
            return null;
        }

        return new StateDto(
                new PositionDto(state.position().x(), state.position().y()),
                DirectionDto.valueOf(state.direction().name())
        );
    }
}
