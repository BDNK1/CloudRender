package tech.theraven.cloudrender.controller.mapper;

import org.mapstruct.Mapper;
import tech.theraven.cloudrender.api.dto.AnalysisInfoDto;
import tech.theraven.cloudrender.domain.AnalysisInfo;

@Mapper(componentModel = "spring")
public interface AnalysisMapper {

    AnalysisInfo toInfo(AnalysisInfoDto dto);
}
