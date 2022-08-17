package tech.theraven.cloudrender.service;


import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.util.UrlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GcpStorageService {

    private final Storage storage;

    private final static String BUCKET_NAME = UrlUtils.getBucketName();

    @SneakyThrows
    public String uploadFile(MultipartFile file, String fileUrl) {
        Bucket bucket = storage.get(BUCKET_NAME);
        var relativeFileUrl = UrlUtils.makeRelative(fileUrl);

        if (file.getSize() < 1_000_000) {
            byte[] bytes = file.getBytes();
            bucket.create(relativeFileUrl, bytes, file.getContentType());
        }

        uploadBigFile(file, relativeFileUrl);
        return fileUrl;
    }

    public boolean checkFiles(long startFrame, long endFrame, String path) {
        Bucket bucket = storage.get(BUCKET_NAME);
        for (long i = startFrame; i < endFrame; i++) {

            if (bucket.get(UrlUtils.getFramePath(path, i, ".jpg")) == null) {
                return false;
            }
        }
        return true;
    }

    private void uploadBigFile(MultipartFile file, String relativeFileUrl) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, relativeFileUrl);
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
    }

}
