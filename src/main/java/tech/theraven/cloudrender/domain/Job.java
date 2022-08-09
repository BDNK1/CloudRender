package tech.theraven.cloudrender.domain;

import lombok.*;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class Job extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String fileUrl;
    Long framesCount;
    Long difficulty;
    @Embedded
    JobSpecs specs;
    @Enumerated(EnumType.STRING)
    JobStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job_id")
    List<WorkUnit> workUnits;
}



