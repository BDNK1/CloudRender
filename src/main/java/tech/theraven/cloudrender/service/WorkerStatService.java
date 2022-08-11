package tech.theraven.cloudrender.service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.Worker;
import tech.theraven.cloudrender.domain.WorkerStat;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;
import tech.theraven.cloudrender.repository.WorkerStatRepository;
import tech.theraven.cloudrender.util.response.BasicErrorType;
import tech.theraven.cloudrender.util.response.Error;
import tech.theraven.cloudrender.util.response.Response;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkerStatService {

    WorkerStatRepository workerStatRepository;

    public Response<WorkUnit> getCurrentWorkUnit(Worker worker) {
        return Response.fromOptional(workerStatRepository.findByWorkerAndStatus(worker, WorkUnitStatus.IN_PROGRESS),
                () -> new Error(BasicErrorType.VALIDATION, "currently no units in progress"))
                .map(WorkerStat::getWorkUnit);

    }


    public Optional<Job> getLastJob(Worker worker) {
        return workerStatRepository.findFirstByWorkerOrderByCreatedOnDesc(worker)
                .map(ws -> ws.getWorkUnit().getJob());
    }


}
