package tech.theraven.cloudrender.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.theraven.cloudrender.api.dto.JobAnalysisDto;
import tech.theraven.cloudrender.api.dto.JobDto;
import tech.theraven.cloudrender.api.dto.JobSpecsDto;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.JobSpecs;

@Mapper(componentModel = "spring")
public interface JobDtoMapper {

    @Mapping(target = "fileName", expression = "java(job.getFileUrl().substring(job.getFileUrl().lastIndexOf(\"/\")+1))")
    JobDto toJobDto(Job job);

    Job toJob(JobAnalysisDto jobDto);

    Job toJob(JobDto jobDto);

    JobSpecs toSpecs(JobSpecsDto jobSpecs);
}
