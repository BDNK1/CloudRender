package tech.theraven.cloudrender.controller.client;

import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.api.client.ClientApi;
import tech.theraven.cloudrender.api.dto.JobDto;
import tech.theraven.cloudrender.api.dto.StartRenderRequest;
import tech.theraven.cloudrender.controller.mapper.JobDtoMapper;
import tech.theraven.cloudrender.domain.JobSpecs;
import tech.theraven.cloudrender.service.JobService;
import tech.theraven.cloudrender.util.DocumentParser;
import tech.theraven.cloudrender.util.Response;

public class ClientController implements ClientApi {

    JobService jobService;

    DocumentParser documentParser;

    JobDtoMapper jobDtoMapper;

    @Override
    public Response<JobDto> upload(MultipartFile file) {
        var document = DocumentParser.parseFromFile(file);
        return jobService.uploadFile(document).map(jobDtoMapper::toJobDto);
    }

    @Override
    public Response<Void> render(StartRenderRequest request) {
        var jobSpecs = jobDtoMapper.toSpecs(request.getSpecs());
        return jobService.startRender(request.getFileId(), jobSpecs);
    }

    @Override
    public Response<Integer> getProgress(Long jobId) {
        return jobService.checkProgress(jobId);
    }
}
