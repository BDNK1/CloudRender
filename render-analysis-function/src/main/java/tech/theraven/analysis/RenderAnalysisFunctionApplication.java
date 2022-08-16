package tech.theraven.analysis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import tech.theraven.analysis.service.JobAnalysisService;
import tech.theraven.analysis.service.WorkStatAnalysisService;

import java.util.Arrays;

@SpringBootApplication
public class RenderAnalysisFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RenderAnalysisFunctionApplication.class, args);
    }

    @Bean
    CommandLineRunner localRunner(JobAnalysisService jobAnalysisService, WorkStatAnalysisService workStatAnalysisService) {
        return args -> {
            jobAnalysisService.analyzeJobs();
            workStatAnalysisService.analyzeWorkUnits();
        };
    }

}
