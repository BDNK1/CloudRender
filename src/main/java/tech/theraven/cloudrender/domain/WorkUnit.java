package tech.theraven.cloudrender.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.theraven.cloudrender.domain.enums.WorkUnitStatus;
import tech.theraven.cloudrender.domain.generic.AuditableEntity;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class    WorkUnit extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_gen")
    @SequenceGenerator(name = "worker_gen", sequenceName = "worker_id_seq")
    Long id;

    Long startFrame;

    Long endFrame;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    Job job;

    @Enumerated(EnumType.STRING)
    WorkUnitStatus status;

}
