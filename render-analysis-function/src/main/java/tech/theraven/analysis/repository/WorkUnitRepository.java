package tech.theraven.analysis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.theraven.analysis.domain.Job;
import tech.theraven.analysis.domain.WorkUnit;
import tech.theraven.analysis.domain.enums.WorkUnitStatus;

import java.util.Optional;

@Repository
public interface WorkUnitRepository extends CrudRepository<WorkUnit, Long> {

    Optional<WorkUnit> findFirstByStatusAndJobOrderByCreatedOnAsc(WorkUnitStatus status, Job job);

    Optional<WorkUnit> findFirstByStatusOrderByCreatedOnAsc(WorkUnitStatus status);
}
