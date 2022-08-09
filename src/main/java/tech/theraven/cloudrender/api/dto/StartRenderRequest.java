package tech.theraven.cloudrender.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartRenderRequest {

    Long fileId;

    JobSpecsDto specs;
}
