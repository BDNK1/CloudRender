package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.theraven.cloudrender.domain.Job;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {

    List<Job> findAll();

    List<Job> findAllByAnalysisIsNullOrderByCreatedOn();

    List<Job> findAllByAnalysisIsNotNull();

    Optional<Job> findFirstByAnalysisIsNullOrderByCreatedOnDesc();

}
