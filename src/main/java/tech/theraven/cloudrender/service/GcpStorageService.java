package tech.theraven.cloudrender.service;


import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.ByteBuffer;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GcpStorageService {

    private final Storage storage;

    private final static String BUCKET_NAME = "cloud_render";

    @SneakyThrows
    public String uploadFile(MultipartFile file) {


        Bucket bucket = storage.get(BUCKET_NAME);

        if (file.getSize() < 1_000_000) {
            byte[] bytes = file.getBytes();
            bucket.create(getPath(file.getOriginalFilename()), bytes, file.getContentType());
            return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + getPath(file.getOriginalFilename());
        }

        BlobId blobId = BlobId.of(bucket.getName(), getPath(file.getOriginalFilename()));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        try (WriteChannel writer = storage.writer(blobInfo)) {

            byte[] buffer = new byte[10_240];
            try (InputStream input = file.getInputStream()) {
                int limit;
                while ((limit = input.read(buffer)) >= 0) {
                    writer.write(ByteBuffer.wrap(buffer, 0, limit));
                }
            }

        }
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + getPath(file.getOriginalFilename());
    }

    public String getPath(String name) {
        return FilenameUtils.getBaseName(name) + "/" + name;
    }

    public String getFramePath(String path, long frame, String resultFileExtension) {
        path = path.replace("https://storage.googleapis.com/cloud_render/", "");
        var extension = "." + FilenameUtils.getExtension(path);
        return path.replace(extension, "_" + String.format("%04d", frame)) + "." + resultFileExtension;
    }

    public boolean checkFiles(long startFrame, long endFrame, String path) {
        Bucket bucket = storage.get(BUCKET_NAME);
        for (long i = startFrame; i < endFrame; i++) {

            if (bucket.get(getFramePath(path, i, "jpg")) == null) {
                return false;
            }
        }
        return true;
    }

}
