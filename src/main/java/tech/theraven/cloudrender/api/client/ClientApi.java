package tech.theraven.cloudrender.api.client;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.api.dto.JobDto;
import tech.theraven.cloudrender.api.dto.StartRenderRequest;
import tech.theraven.cloudrender.util.Response;

@RequestMapping("/api/client")
public interface ClientApi {

    @PostMapping("/render/upload")
    Response<JobDto> upload(@RequestBody MultipartFile file);

    @PostMapping("/render/start")
    Response<Void> render(@RequestBody StartRenderRequest request);

    @GetMapping("/render/progress")
    Response<Integer> getProgress(@RequestParam Long jobId);

}
