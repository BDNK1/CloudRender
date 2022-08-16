package tech.theraven.analysis.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import tech.theraven.analysis.domain.AnalysisInfo;
import tech.theraven.analysis.repository.JobRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobAnalysisService {

    JobRepository jobRepository;

    GcpStorageService gcpStorageService;

    public void analyzeJobs() {
        var jobsToAnalyze = jobRepository.findAllByAnalysisIsNullOrderByCreatedOn();
        jobsToAnalyze.stream()
                .map(j -> Pair.of(j, gcpStorageService.downloadFile(j.getFileUrl())))
                .map(pair -> Pair.of(pair.getFirst(), getNumberOfFrames(pair.getSecond())))
                .forEach(pair -> {
                    var job = pair.getFirst();
                    job.setAnalysis(new AnalysisInfo(pair.getSecond(), 0L));
                    jobRepository.save(job);
                });
    }


    @SneakyThrows
    private Long getNumberOfFrames(String blendFilePath) {

        String pyPath = "number.py";

        ClassLoader classLoader = getClass().getClassLoader();
        File blendFile = new File(blendFilePath);
        File pyFile = new File(classLoader.getResource(pyPath).getFile());
        String absoluteBlendPath = blendFile.getAbsolutePath();
        String absolutePyPath = pyFile.getAbsolutePath();

        ProcessBuilder processBuilder = new ProcessBuilder("blender", "-b", absoluteBlendPath, "--python", absolutePyPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        return readProcessOutput(process.getInputStream());

    }

    @SneakyThrows
    private Long readProcessOutput(InputStream inputStream) {
        var str = new BufferedReader(new InputStreamReader(inputStream)).readLine();

        return Long.valueOf(str);
    }
}
