package tech.theraven.cloudrender.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.theraven.cloudrender.domain.JobSpecs;
import tech.theraven.cloudrender.domain.enums.JobStatus;
import tech.theraven.cloudrender.domain.generic.AuditableEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_id_seq")
    @SequenceGenerator(name = "job_id_seq", sequenceName = "job_id_seq")
    Long id;
    String fileUrl;
    Long framesCount;
    Long difficulty;
    @Embedded
    JobSpecs specs;
    @Enumerated(EnumType.STRING)
    JobStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    List<WorkUnit> workUnits;
}



