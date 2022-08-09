package tech.theraven.cloudrender.controller.analysis;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import tech.theraven.cloudrender.api.dto.JobAnalysisDto;
import tech.theraven.cloudrender.controller.mapper.JobDtoMapper;
import tech.theraven.cloudrender.domain.entity.Job;
import tech.theraven.cloudrender.service.JobService;

@Controller
public class AnalysisController {

    JobService jobService;
    JobDtoMapper jobDtoMapper;

    @MessageMapping("/hello")
    public Job analyze(JobAnalysisDto jobDto) {
        var job = jobDtoMapper.toJob(jobDto);
        return jobService.update(job);
    }

}
