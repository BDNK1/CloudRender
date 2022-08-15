package tech.theraven.cloudrender.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.theraven.cloudrender.api.dto.WorkUnitDto;
import tech.theraven.cloudrender.domain.WorkUnit;


@Mapper(componentModel = "spring")

public interface WorkUnitMapper {

    @Mapping(target="fileUrl", expression = "java(workUnit.getJob().getFileUrl())")
    @Mapping(target="specs.engine", expression = "java(workUnit.getJob().getSpecs().getEngine())")
    WorkUnitDto toDto(WorkUnit workUnit);
}
