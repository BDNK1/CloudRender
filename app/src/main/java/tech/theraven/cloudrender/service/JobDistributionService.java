package tech.theraven.cloudrender.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.Worker;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;
import tech.theraven.cloudrender.util.response.BasicErrorType;
import tech.theraven.cloudrender.util.response.Error;
import tech.theraven.cloudrender.util.response.Response;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobDistributionService {

    WorkerStatService workerStatService;
    WorkUnitService workUnitService;
    JobEntityService jobEntityService;


    public Response<WorkUnit> getWorkUnit(Worker worker) {

        //TODO: refactor
        if (workerStatService.getCurrentWorkUnit(worker).isSuccess()) {
            return Response.error(new Error(BasicErrorType.VALIDATION, "you already have a job"));
        }

        Response<WorkUnit> lastJobWorkUnit = findWorkUnitFromLastJob(worker);
        if (lastJobWorkUnit.isSuccess()) return lastJobWorkUnit;

        Optional<WorkUnit> workUnit = workUnitService.findAvailableWorkUnit();
        if (workUnit.isPresent()) {
            workUnitService.changeStatus(workUnit.get(), WorkUnitStatus.IN_PROGRESS);
            workerStatService.create(worker, workUnit.get());
            return Response.of(workUnit.get());
        }

        return generateWorkUnitsAndGet(worker);
    }

    private Response<WorkUnit> generateWorkUnitsAndGet(Worker worker) {
        List<Job> analizedJobsWithoutWorkUnits = jobEntityService.findAvailableAndAnalizedJobsWithoutWorkUnits();
        var workUnits = workUnitService.brokeIntoWorkUnits(analizedJobsWithoutWorkUnits);
        Optional<WorkUnit> min = workUnits.stream().max(Comparator.comparing(wu->wu.getJob().getPrice()));
        return Response.fromOptional(min, () -> new Error(BasicErrorType.UNEXPECTED, "no work, comeback later"))
                .peek(wu -> workUnitService.changeStatus(wu, WorkUnitStatus.IN_PROGRESS))
                .peek(wu -> workerStatService.create(worker, min.get()));
    }

    private Response<WorkUnit> findWorkUnitFromLastJob(Worker worker) {
        Optional<Job> lastJob = workerStatService.getLastJob(worker);
        if (lastJob.isPresent()) {
            Optional<WorkUnit> lastJobWorkUnit = workUnitService.findAvailableWorkUnit(lastJob.get());
            if (lastJobWorkUnit.isPresent()) {
                workUnitService.changeStatus(lastJobWorkUnit.get(), WorkUnitStatus.IN_PROGRESS);
                workerStatService.create(worker, lastJobWorkUnit.get());
                return Response.of(lastJobWorkUnit.get());
            }
        }
        return Response.error(new Error(BasicErrorType.UNEXPECTED, "no workunit from last job"));
    }

}
