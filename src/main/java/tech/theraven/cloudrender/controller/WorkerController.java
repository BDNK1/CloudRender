package tech.theraven.cloudrender.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;
import tech.theraven.cloudrender.api.WorkerApi;
import tech.theraven.cloudrender.api.dto.*;
import tech.theraven.cloudrender.controller.mapper.WorkUnitMapper;
import tech.theraven.cloudrender.controller.mapper.WorkerMapper;
import tech.theraven.cloudrender.domain.Worker;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;
import tech.theraven.cloudrender.service.JobDistributionService;
import tech.theraven.cloudrender.service.WorkUnitService;
import tech.theraven.cloudrender.service.WorkerService;
import tech.theraven.cloudrender.util.response.BasicErrorType;
import tech.theraven.cloudrender.util.response.Error;
import tech.theraven.cloudrender.util.response.Response;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkerController implements WorkerApi {

    WorkerService workerService;
    JobDistributionService jobDistributionService;
    WorkUnitService workUnitService;

    WorkerMapper workerMapper;
    WorkUnitMapper workUnitMapper;


    @Override
    public Response<WorkerRegisterResponse> register() {
        return workerService.register().map(workerMapper::toWorkerRegisterResponse);
    }

    @Override
    public Response<WorkUnitDto> getWorkUnit(Long workerId) {
        return workerService.findById(workerId)
                .flatMap(w -> jobDistributionService.getWorkUnit(w)
                        .map(workUnitMapper::toDto));
    }

    @Override
    public Response<Void> confirmResult(Long workerId) {
        return workerService.findById(workerId)
                .flatMap(workerService::confirmResult);

    }

    @Override
    public Response<Void> updateWorkerStats(Long workerId) {
        return workerService.findById(workerId)
                .flatMap(w -> workerService.updateStatus(w, WorkUnitStatus.IN_PROGRESS));
    }

}
