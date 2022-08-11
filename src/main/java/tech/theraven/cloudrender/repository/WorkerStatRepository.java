package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import tech.theraven.cloudrender.domain.Worker;
import tech.theraven.cloudrender.domain.WorkerStat;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;

import java.util.Optional;

public interface WorkerStatRepository extends CrudRepository<WorkerStat, Long> {

    Optional<WorkerStat> findFirstByWorkerOrderByCreatedOnDesc(Worker worker);

    Optional<WorkerStat> findByWorkerAndStatus(Worker worker, WorkUnitStatus status);
}
