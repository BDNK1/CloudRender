package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkUnitRepository extends CrudRepository<WorkUnit, Long> {

    Optional<WorkUnit> findFirstByStatusAndJobOrderByJobPriceDesc(WorkUnitStatus status, Job job);

    Optional<WorkUnit> findFirstByStatusOrderByJobPriceDesc(WorkUnitStatus status);

}
