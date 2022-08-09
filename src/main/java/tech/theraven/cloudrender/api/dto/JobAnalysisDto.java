package tech.theraven.cloudrender.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobAnalysisDto {
    Long id;
    String fileUrl;
    Long framesCount;
    Long difficulty;
}
