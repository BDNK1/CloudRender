package tech.theraven.cloudrender.controller.client;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.api.client.ClientApi;
import tech.theraven.cloudrender.api.dto.JobDto;
import tech.theraven.cloudrender.api.dto.StartRenderRequest;
import tech.theraven.cloudrender.controller.mapper.JobDtoMapper;
import tech.theraven.cloudrender.service.JobService;
import tech.theraven.cloudrender.util.DocumentParser;
import tech.theraven.cloudrender.util.response.Response;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientController implements ClientApi {

    @Autowired
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
    public Response<Long> getProgress(Long jobId) {
        return jobService.checkProgress(jobId);
    }
}
