package tech.theraven.cloudrender.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartRenderRequest {

    Long jobId;

    JobSpecsDto specs;
}
