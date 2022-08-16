package tech.theraven.analysis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.theraven.analysis.domain.generic.AuditableEntity;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_gen")
    @SequenceGenerator(name = "worker_gen", sequenceName = "worker_id_seq")
    Long id;

}
