package tech.theraven.analysis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.theraven.analysis.domain.enums.WorkerStatStatus;
import tech.theraven.analysis.domain.generic.AuditableEntity;

import javax.persistence.*;

@Entity
@Data
@Builder
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
    @Enumerated(EnumType.STRING)
    WorkerStatStatus status;
}
