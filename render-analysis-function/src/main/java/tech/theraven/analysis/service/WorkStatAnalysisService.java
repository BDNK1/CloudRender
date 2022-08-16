package tech.theraven.analysis.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.analysis.domain.WorkerStat;
import tech.theraven.analysis.domain.enums.WorkUnitStatus;
import tech.theraven.analysis.domain.enums.WorkerStatStatus;
import tech.theraven.analysis.repository.WorkUnitRepository;
import tech.theraven.analysis.repository.WorkerStatRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkStatAnalysisService {
    WorkerStatRepository workerStatRepository;
    WorkUnitRepository workUnitRepository;

    public void analyzeWorkUnits() {
        var failedJobs = workerStatRepository.findAllByStatusAndTimePassed(WorkerStatStatus.IN_PROGRESS.toString());
        failedJobs.forEach(j -> j.setStatus(WorkerStatStatus.FAILED));
        var failedWorkUnits = failedJobs.stream()
                .map(WorkerStat::getWorkUnit)
                .peek(wu -> wu.setStatus(WorkUnitStatus.AVAILABLE))
                .toList();

        workUnitRepository.saveAll(failedWorkUnits);
        workerStatRepository.saveAll(failedJobs);
    }
}
