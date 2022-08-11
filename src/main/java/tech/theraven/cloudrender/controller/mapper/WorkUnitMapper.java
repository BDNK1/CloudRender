package tech.theraven.cloudrender.controller.mapper;

import org.mapstruct.Mapper;
import tech.theraven.cloudrender.api.dto.WorkUnitDto;
import tech.theraven.cloudrender.domain.WorkUnit;


@Mapper(componentModel = "spring")

public interface WorkUnitMapper {

    WorkUnitDto toDto(WorkUnit workUnit);
}
