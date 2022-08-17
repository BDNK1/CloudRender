package tech.theraven.analysis.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
        var relativePath = path.replace("https://storage.googleapis.com/" + BUCKET_NAME + "/", "");
        Path pathToDownload = Path.of("/downloads/", relativePath);

        Bucket bucket = storage.get(BUCKET_NAME);
        Blob file = bucket.get(relativePath);
        Files.createDirectories(pathToDownload.getParent());
        Files.createFile(pathToDownload);
        pathToDownload.toFile().deleteOnExit();
        file.downloadTo(pathToDownload);
        return pathToDownload.toAbsolutePath().toString();
    }


}
