package tech.theraven.cloudrender.service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.Worker;
import tech.theraven.cloudrender.domain.WorkerStat;
import tech.theraven.cloudrender.domain.enums.WorkerStatStatus;
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

    public WorkerStat create(Worker worker, WorkUnit workUnit) {
        var workerStat = WorkerStat.builder()
                .worker(worker)
                .workUnit(workUnit)
                .status(WorkerStatStatus.IN_PROGRESS)
                .build();
        return workerStatRepository.save(workerStat);
    }

    public Response<WorkUnit> getCurrentWorkUnit(Worker worker) {
        return Response.fromOptional(getCurrentStat(worker),
                        () -> new Error(BasicErrorType.VALIDATION, "currently no units in progress"))
                .map(WorkerStat::getWorkUnit);
    }

    public Optional<Job> getLastJob(Worker worker) {
        return workerStatRepository.findFirstByWorkerOrderByCreatedOnDesc(worker)
                .map(ws -> ws.getWorkUnit().getJob());
    }

    public Response<Void> changeCurrentStatus(Worker worker, WorkerStatStatus workerStatStatus) {
        var currentStat = getCurrentStat(worker);
        if (currentStat.isPresent()) {
            currentStat.get().setStatus(workerStatStatus);
            workerStatRepository.save(currentStat.get());
        }
        return Response.error(new Error(BasicErrorType.UNEXPECTED, "no work stat"));
    }

    private Optional<WorkerStat> getCurrentStat(Worker worker) {
        return workerStatRepository.findByWorkerAndStatus(worker, WorkerStatStatus.IN_PROGRESS);
    }
}
