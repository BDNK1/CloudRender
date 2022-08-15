package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.enums.JobStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {

    @Override
    List<Job> findAll();

    List<Job> findAllByAnalysisIsNullOrderByCreatedOn();

    List<Job> findAllByAnalysisIsNotNullAndStatus(JobStatus status);

    Optional<Job> findFirstByAnalysisIsNullOrderByCreatedOnDesc();

}
