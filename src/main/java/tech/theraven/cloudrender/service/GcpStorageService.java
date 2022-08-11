package tech.theraven.cloudrender.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.theraven.cloudrender.api.dto.GoogleDocumentDto;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GcpStorageService {

    private final Storage storage;

    private final static String BUCKET_NAME = "cloud_render";

    public String uploadFile(GoogleDocumentDto doc) {
        Bucket bucket = storage.get(BUCKET_NAME);
        Blob blenderFileBlob = bucket.create(getPath(doc.getName()), doc.getContent(), doc.getContentType());

        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + getPath(doc.getName());
    }

    public String getPath(String name) {
        System.out.println(name);
        return name + "/" + name;
    }

    public String getFramePath(String path, long frame) {
        return path + "_" + frame;
    }

    public boolean checkFiles(long startFrame, long endFrame, String path) {
        Bucket bucket = storage.get(BUCKET_NAME);
        for (long i = startFrame; i < endFrame; i++) {
            Blob blenderFileBlob = bucket.get(getFramePath(path, i));
            if (!blenderFileBlob.exists()) {
                return false;
            }
        }
        return true;
    }

}
