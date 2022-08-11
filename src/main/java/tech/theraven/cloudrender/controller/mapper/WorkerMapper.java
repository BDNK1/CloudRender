package tech.theraven.cloudrender.controller.mapper;

import org.mapstruct.Mapper;
import tech.theraven.cloudrender.api.dto.WorkerInfoDto;
import tech.theraven.cloudrender.api.dto.WorkerRegisterResponse;
import tech.theraven.cloudrender.domain.Worker;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

    Worker toWorker(WorkerInfoDto workerInfoDto);

    WorkerRegisterResponse toWorkerRegisterResponse(Worker worker);
}
