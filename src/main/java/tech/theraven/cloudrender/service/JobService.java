package tech.theraven.cloudrender.service;

import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.api.dto.GoogleDocumentDto;
import tech.theraven.cloudrender.api.dto.JobSpecsDto;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.JobSpecs;
import tech.theraven.cloudrender.domain.JobStatus;
import tech.theraven.cloudrender.domain.Status;
import tech.theraven.cloudrender.repository.JobRepository;
import tech.theraven.cloudrender.util.BasicErrorType;
import tech.theraven.cloudrender.util.Response;

@Service
public class JobService {

    GcpStorageService gcpStorageService;

    JobRepository jobRepository;

    public Response<Void> startRender(Long jobId, JobSpecs specs) {
        var jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            return Response.error(BasicErrorType.AUTHORIZATION, "no such job");
        }
        var job = jobOptional.get();
        if (job.getStatus() == JobStatus.DONE || job.getStatus() == JobStatus.AVAILABLE) {
            return Response.error(BasicErrorType.VALIDATION, "Job already in progress");
        }
        job.setStatus(JobStatus.AVAILABLE);
        job.setSpecs(specs);
        jobRepository.save(job);
        return Response.empty();
    }

    public Response<Integer> checkProgress(Long jobId) {
        var jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            return Response.error(BasicErrorType.AUTHORIZATION, "no such job");
        }
        var workUnits = jobOptional.get().getWorkUnits();

        return Response.of(workUnits.stream()
                .mapToInt(w -> w.getStatus() == Status.DONE ? 1 : 0)
                .sum() / workUnits.size()
        );
    }

    public Response<Job> uploadFile(GoogleDocumentDto doc) {
        var fileUrl = gcpStorageService.uploadFile(doc);
        //TODO: send this event through websocket to our machine for analysis and updating info
        return Response.of(createJob(fileUrl));
    }

    private Job createJob(String fileUrl) {
        var job = Job.builder()
                .status(JobStatus.NEW)
                .fileUrl(fileUrl)
                .build();
        return jobRepository.save(job);
    }

    public Job update(Job job) {
        return jobRepository.save(job);
    }
}
