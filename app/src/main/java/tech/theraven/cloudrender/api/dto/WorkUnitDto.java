package tech.theraven.cloudrender.api.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkUnitDto {

    String fileUrl;
    String uploadUrl;
    Long startFrame;
    Long endFrame;
    JobSpecsDto specs;

}
