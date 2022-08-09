package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import tech.theraven.cloudrender.domain.Job;

public interface JobRepository extends CrudRepository<Job, Long> {
}