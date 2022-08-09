package tech.theraven.cloudrender.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "content")
public class GoogleDocumentDto {

    private String name;
    private String contentType;
    private String extension;
    private byte[] content;

}
