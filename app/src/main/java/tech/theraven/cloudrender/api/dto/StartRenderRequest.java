package tech.theraven.cloudrender.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class StartRenderRequest {

    @NotNull
    Long jobId;
    @NotNull
    Long price;

    JobSpecsDto specs;
}
