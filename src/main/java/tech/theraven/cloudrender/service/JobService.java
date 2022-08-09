package tech.theraven.cloudrender.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.api.dto.GoogleDocumentDto;
import tech.theraven.cloudrender.domain.entity.Job;
import tech.theraven.cloudrender.domain.JobSpecs;
import tech.theraven.cloudrender.domain.enums.JobStatus;
import tech.theraven.cloudrender.domain.enums.WorkerUnitStatus;
import tech.theraven.cloudrender.repository.JobRepository;
import tech.theraven.cloudrender.util.response.BasicErrorType;
import tech.theraven.cloudrender.util.response.Response;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

    public Response<Long> checkProgress(Long jobId) {
        var jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            return Response.error(BasicErrorType.VALIDATION, "no such job");
        }
        var workUnits = jobOptional.get().getWorkUnits();
        if (jobOptional.get().getStatus() == JobStatus.NEW) {
            return Response.error(BasicErrorType.VALIDATION, "rendering isn't started");
        }
        if (workUnits == null) {
            return Response.of(0L);
        }
        return Response.of(workUnits.stream()
                .filter(w -> w.getStatus() == WorkerUnitStatus.DONE).count() / workUnits.size()
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
