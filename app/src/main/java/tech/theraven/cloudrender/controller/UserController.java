package tech.theraven.cloudrender.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.api.UserApi;
import tech.theraven.cloudrender.api.dto.*;
import tech.theraven.cloudrender.controller.mapper.JobDtoMapper;
import tech.theraven.cloudrender.service.JobService;
import tech.theraven.cloudrender.service.UserService;
import tech.theraven.cloudrender.util.UrlUtils;
import tech.theraven.cloudrender.util.response.Response;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController implements UserApi {

    JobService jobService;
    JobDtoMapper jobDtoMapper;
    UserService userService;

    @Override
    public Response<UserRegisterResponse> register(UserRegisterRequest request) {
        return null;
    }

    @Override
    public Response<LoginResponse> login(Long userToken) {
        return null;
    }

    @Override
    public Response<JobDto> upload(MultipartFile file, Long userId) {
        return userService.getById(userId)
                .map(user -> UrlUtils.generateFileUrl(file.getOriginalFilename(), user.getEmail()))
                .flatMap(fileUrl -> jobService.createJob(file, fileUrl))
                .map(jobDtoMapper::toJobDto);
    }

    @Override
    public Response<Void> render(StartRenderRequest request) {
        var jobSpecs = jobDtoMapper.toSpecs(request.getSpecs());
        return jobService.setJobAvailable(request.getJobId(), request.getPrice(), jobSpecs);
    }

    @Override
    public Response<Long> getProgress(Long jobId) {
        return jobService.checkProgress(jobId);
    }

    @Override
    public Response<String> getResult(Long jobId) {
        return jobService.getResult(jobId);
    }

}
