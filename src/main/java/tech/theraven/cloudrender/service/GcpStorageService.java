package tech.theraven.cloudrender.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.api.dto.GoogleDocumentDto;

@Service
@RequiredArgsConstructor
public class GcpStorageService {

    private final Storage storage;

    @Value("gs://[YOUR_GCS_BUCKET]/[GCS_FILE_NAME]")
    private String path;

    private String bucketName;

    public String uploadFile(GoogleDocumentDto doc) {
        Bucket bucket = storage.get(bucketName);
        Blob blenderFileBlob = bucket.create(getPath(doc.getName()), doc.getContent(), doc.getContentType());

        return "https://storage.googleapis.com/" + bucketName + "/" + blenderFileBlob.getName();
    }

    public String getPath(String name) {
        return name;
    }
}
