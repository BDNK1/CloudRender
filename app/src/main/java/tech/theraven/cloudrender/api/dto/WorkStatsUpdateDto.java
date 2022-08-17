package tech.theraven.cloudrender.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkStatsUpdateDto {

    Long currentFrame;

}
