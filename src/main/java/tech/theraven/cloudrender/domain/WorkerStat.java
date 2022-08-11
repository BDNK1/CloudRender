package tech.theraven.cloudrender.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.theraven.cloudrender.domain.enums.WorkerStatStatus;
import tech.theraven.cloudrender.domain.generic.AuditableEntity;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerStat extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_stat_gen")
    @SequenceGenerator(name = "worker_stat_gen", sequenceName = "worker_stat_id_seq")
    Long id;
    @OneToOne
    WorkUnit workUnit;
    @OneToOne
    Worker worker;

    WorkerStatStatus status;
}
