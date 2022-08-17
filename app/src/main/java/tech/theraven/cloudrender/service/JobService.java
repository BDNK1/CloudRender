package tech.theraven.cloudrender.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.JobSpecs;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.enums.JobStatus;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;
import tech.theraven.cloudrender.util.response.BasicErrorType;
import tech.theraven.cloudrender.util.response.Error;
import tech.theraven.cloudrender.util.response.Response;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobService {

    GcpStorageService gcpStorageService;

    JobEntityService jobEntityService;

    public Response<Job> createJob(MultipartFile file, String fileUrl) {
        return validateFile(file)
                .map(f -> gcpStorageService.uploadFile(file, fileUrl))
                .map(jobEntityService::createJob);
    }

    public Response<Void> setJobAvailable(Long jobId, Long price, JobSpecs specs) {
        var jobOptional = jobEntityService.findById(jobId);
        if (jobOptional.isEmpty()) {
            return Response.error(BasicErrorType.AUTHORIZATION, "no such job");
        }
        var job = jobOptional.get();
        if (job.getStatus() == JobStatus.DONE || job.getStatus() == JobStatus.AVAILABLE) {
            return Response.error(BasicErrorType.VALIDATION, "Job already in progress");
        }
        job.setPrice(price);
        job.setStatus(JobStatus.AVAILABLE);
        job.setSpecs(specs);
        jobEntityService.update(job);
        return Response.empty();
    }

    public Response<Long> checkProgress(Long jobId) {
        var jobOptional = jobEntityService.findById(jobId);
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
        double doneCount = workUnits.stream()
                .filter(w -> w.getStatus() == WorkUnitStatus.DONE).count();
        return Response.of((long) (doneCount / workUnits.size() * 100));
    }


    public Response<String> getResult(Long jobId) {
        var jobOptional = jobEntityService.findById(jobId);
        if (jobOptional.isEmpty()) {
            return Response.error(BasicErrorType.VALIDATION, "no such job");
        }
        if (jobOptional.get().getStatus() != JobStatus.DONE) {
            return Response.error(new Error(BasicErrorType.UNEXPECTED, "job isnt done yet"));
        }

        return Response.of(jobOptional.get().getFileUrl());
    }

    public void checkIfJobDone(Job job) {
        List<WorkUnit> workUnits = job.getWorkUnits();
        if (workUnits.stream().filter(wu -> wu.getStatus() == WorkUnitStatus.DONE).count() == workUnits.size()) {
            job.setStatus(JobStatus.DONE);
            jobEntityService.update(job);
        }
    }

    private Response<MultipartFile> validateFile(MultipartFile file) {
        var extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension != null && !extension.equals("blend")) {
            System.out.println(extension);
            return Response.error(new Error(BasicErrorType.VALIDATION, "Currently we support only blend files"));
        }
        return Response.of(file);
    }
}
