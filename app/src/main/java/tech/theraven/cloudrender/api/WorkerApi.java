package tech.theraven.cloudrender.api;

import org.springframework.web.bind.annotation.*;
import tech.theraven.cloudrender.api.dto.*;
import tech.theraven.cloudrender.util.response.Response;

@RequestMapping("/api/worker")
public interface WorkerApi {

    @PostMapping("/register")
    Response<WorkerRegisterResponse> register();

    @GetMapping("/job")
    Response<WorkUnitDto> getWorkUnit(@RequestHeader("Authorization") Long workerId);

    @PostMapping("/done")
    Response<Void> confirmResult(@RequestHeader("Authorization") Long workerId);

    @PostMapping("/update")
    Response<Void> updateWorkerStats(@RequestHeader("Authorization") Long workerId, WorkStatsUpdateDto updateDto);

    @GetMapping("/balance")
    Response<WorkerBalanceDto> getBalance(@RequestHeader("Authorization") Long workerId);

}
