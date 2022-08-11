package tech.theraven.cloudrender.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleDocumentDto {

    private String name;
    private String contentType;
    private String extension;
    private byte[] content;

}
