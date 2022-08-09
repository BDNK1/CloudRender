package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import tech.theraven.cloudrender.domain.WorkUnit;

public interface WorkUnitRepository extends CrudRepository<WorkUnit, Long> {
}
