package tech.theraven.cloudrender.service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.enums.JobStatus;
import tech.theraven.cloudrender.repository.JobRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobEntityService {

    JobRepository jobRepository;

    public Job createJob(String fileUrl) {
        var job = Job.builder()
                .status(JobStatus.NEW)
                .fileUrl(fileUrl)
                .build();
        return jobRepository.save(job);
    }

    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Job update(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> findAllUnanalized() {
        return jobRepository.findAllByAnalysisIsNullOrderByCreatedOn();
    }

    public List<Job> findAvailableAndAnalizedJobsWithoutWorkUnits() {
        return jobRepository.findAllByAnalysisIsNotNullAndStatus(JobStatus.AVAILABLE).stream()
                .filter(j -> j.getWorkUnits().isEmpty() )
                .peek(System.out::println)
                .collect(Collectors.toList());
    }
}
