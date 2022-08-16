package tech.theraven.analysis.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tech.theraven.analysis.domain.Worker;
import tech.theraven.analysis.domain.WorkerStat;
import tech.theraven.analysis.domain.enums.WorkerStatStatus;

import java.util.List;
import java.util.Optional;

public interface WorkerStatRepository extends CrudRepository<WorkerStat, Long> {

    Optional<WorkerStat> findFirstByWorkerOrderByCreatedOnDesc(Worker worker);

    @Query(value = """
            SELECT * FROM render.worker_stat
                  WHERE status = :status
                  AND last_modified_on < NOW() - INTERVAL '10 minutes'
            """, nativeQuery = true)
    List<WorkerStat> findAllByStatusAndTimePassed(@Param("status") String statusString);

    Optional<WorkerStat> findByWorkerAndStatus(Worker worker, WorkerStatStatus status);
}
