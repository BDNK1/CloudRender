package tech.theraven.cloudrender.util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tech.theraven.cloudrender.api.dto.GoogleDocumentDto;

import java.io.IOException;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentParser {

    @SneakyThrows(IOException.class)
    public static GoogleDocumentDto parseFromFile(MultipartFile file) {
        GoogleDocumentDto document = new GoogleDocumentDto();
        document.setName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setExtension(file.getContentType());
        document.setContent(file.getBytes());
        return document;
    }

}