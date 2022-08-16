package tech.theraven.analysis.service;


import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GcpStorageService {

    private final Storage storage;

    private final static String BUCKET_NAME = "cloud_render";

    @SneakyThrows
    public String downloadFile(String path) {
        //TODO: refactor
        Bucket bucket = storage.get(BUCKET_NAME);
        path = path.replace("https://storage.googleapis.com/" + BUCKET_NAME + "/", "");
        Path pathToDownload = Path.of("/downloads/", path);
        var file = bucket.get(path);
        Files.createDirectories(pathToDownload.getParent());
        Files.createFile(pathToDownload);
        pathToDownload.toFile().deleteOnExit();
        file.downloadTo(pathToDownload);
        return pathToDownload.toString();

    }

}
