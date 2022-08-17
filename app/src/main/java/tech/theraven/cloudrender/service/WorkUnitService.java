package tech.theraven.cloudrender.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;
import tech.theraven.cloudrender.repository.WorkUnitRepository;
import tech.theraven.cloudrender.util.response.BasicErrorType;
import tech.theraven.cloudrender.util.response.Error;
import tech.theraven.cloudrender.util.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkUnitService {

    final GcpStorageService gcpStorageService;
    final WorkUnitRepository workUnitRepository;

    @Value("${job-distribution.framesPerUnit}")
    long framesPerUnit;

    public Optional<WorkUnit> findAvailableWorkUnit() {
        return workUnitRepository.findFirstByStatusOrderByJobPriceDesc(WorkUnitStatus.AVAILABLE);
    }

    public Optional<WorkUnit> findAvailableWorkUnit(Job job) {
        return workUnitRepository.findFirstByStatusAndJobOrderByJobPriceDesc(WorkUnitStatus.AVAILABLE, job);
    }

    public void changeStatus(WorkUnit workUnit, WorkUnitStatus status) {
        workUnit.setStatus(status);
        workUnitRepository.save(workUnit);
    }

    public List<WorkUnit> brokeIntoWorkUnits(List<Job> jobs) {
        var workUnits = jobs.stream()
                .map(this::brokeIntoWorkUnits)
                .filter(Response::isSuccess)
                .flatMap(r -> r.getData().stream())
                .toList();
        workUnitRepository.saveAll(workUnits);
        return workUnits;
    }

    Response<List<WorkUnit>> brokeIntoWorkUnits(Job job) {
        if (!job.getWorkUnits().isEmpty() || !job.isAnalized()) {
            return Response.error(new Error(BasicErrorType.VALIDATION, "job already broken into work units"));
        }
        List<WorkUnit> workUnits = new ArrayList<>();
        Long framesCount = job.getAnalysis().getFramesCount();
        long unitsCount = framesCount / framesPerUnit;

        Stream.iterate(0L, n -> ++n)
                .limit(unitsCount)
                .forEach(n -> {
                    WorkUnit unit = WorkUnit.builder()
                            .job(job)
                            .startFrame(n * framesPerUnit)
                            .endFrame(++n * framesPerUnit - 1)
                            .status(WorkUnitStatus.AVAILABLE)
                            .build();
                    workUnits.add(unit);
                });

        distributeOverflow(workUnits, framesCount % framesPerUnit);
        System.out.println(workUnits);
        return Response.of(workUnits);

    }

    private static void distributeOverflow(List<WorkUnit> workUnits, long overflow) {
        int unitsCount = workUnits.size();

        long itemsPerBucket = (overflow / unitsCount);
        long remainingItems = (overflow % unitsCount);

        while (remainingItems > 0) {
            WorkUnit curr = workUnits.get(unitsCount - 1);
            Long startFrame = curr.getStartFrame();
            Long endFrame = curr.getEndFrame();

            curr.setEndFrame(endFrame + remainingItems + itemsPerBucket);
            curr.setStartFrame(startFrame + remainingItems + itemsPerBucket - 1);
            remainingItems--;
            unitsCount--;
        }
    }


    public Response<WorkUnit> confirmResult(WorkUnit workUnit) {

        String path = workUnit.getJob().getFileUrl();
        if (gcpStorageService.checkFiles(workUnit.getStartFrame(), workUnit.getEndFrame(), path)) {
            changeStatus(workUnit, WorkUnitStatus.DONE);
            return Response.of(workUnit);
        }
        changeStatus(workUnit, WorkUnitStatus.AVAILABLE);
        return Response.error(new Error(BasicErrorType.UNEXPECTED, "no files"));
    }
}
