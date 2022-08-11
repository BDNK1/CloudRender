package tech.theraven.cloudrender.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisInfo {


    Long framesCount;
    Long difficulty;
}
