package tech.theraven.cloudrender.service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.domain.Worker;
import tech.theraven.cloudrender.domain.enums.WorkerStatStatus;
import tech.theraven.cloudrender.repository.WorkerRepository;
import tech.theraven.cloudrender.util.response.BasicErrorType;
import tech.theraven.cloudrender.util.response.Error;
import tech.theraven.cloudrender.util.response.Response;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkerService {

    WorkUnitService workUnitService;
    WorkerStatService workerStatService;
    WorkerRepository workerRepository;
    JobService jobService;

    public Response<Worker> register() {
        return Response.of(workerRepository.save(new Worker()));
    }

    public Response<Worker> findById(Long id) {
        return Response.fromOptional(workerRepository.findById(id),
                () -> new Error(BasicErrorType.VALIDATION, "no such worker"));
    }

    public Response<Void> confirmResult(Worker worker) {
        return workerStatService.getCurrentWorkUnit(worker)
                .flatMap(workUnitService::confirmResult)
                .peek(wu -> workerStatService.changeCurrentStatus(worker, WorkerStatStatus.DONE))
                .peekOnError(wu -> workerStatService.changeCurrentStatus(worker, WorkerStatStatus.FAILED))
                .peek(wu -> jobService.checkIfJobDone(wu.getJob()))
                .andThen(Response::empty);
    }

    public Response<Void> updateWorkerStat(Worker worker, WorkerStatStatus status) {
        return workerStatService.getCurrentWorkUnit(worker)
                .peek(w -> workerStatService.changeCurrentStatus(worker, status))
                .andThen(Response::empty);
    }

}
