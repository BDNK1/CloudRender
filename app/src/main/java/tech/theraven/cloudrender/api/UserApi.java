package tech.theraven.cloudrender.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.api.dto.*;
import tech.theraven.cloudrender.util.response.Response;

@RequestMapping("/api/user")
public interface UserApi {

    @PostMapping ("/register")
    Response<UserRegisterResponse> register(@RequestBody UserRegisterRequest request);


    @PostMapping("/login")
    Response<LoginResponse> login(@RequestHeader("Authorization") Long userToken);

    @PostMapping("/render/upload")
    Response<JobDto> upload(@RequestParam MultipartFile file, @RequestHeader("Authorization") Long userId);

    @PostMapping("/render/start")
    Response<Void> render(@RequestBody StartRenderRequest request);

    @GetMapping("/render/progress")
    Response<Long> getProgress(@RequestParam Long jobId);

    @GetMapping("/render/result")
    Response<String> getResult(@RequestParam Long jobId);

}
