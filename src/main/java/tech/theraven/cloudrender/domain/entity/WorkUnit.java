package tech.theraven.cloudrender.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import tech.theraven.cloudrender.domain.enums.WorkerUnitStatus;
import tech.theraven.cloudrender.domain.generic.AuditableEntity;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkUnit extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_gen")
    @SequenceGenerator(name = "worker_gen", sequenceName = "worker_id_seq")
    Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    Job job;

    @Enumerated(EnumType.STRING)
    WorkerUnitStatus status;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    Worker worker;

}
