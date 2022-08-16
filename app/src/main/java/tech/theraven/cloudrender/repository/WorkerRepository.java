package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.theraven.cloudrender.domain.Worker;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {
}
