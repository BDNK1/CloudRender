package tech.theraven.cloudrender.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import tech.theraven.cloudrender.domain.generic.AuditableEntity;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_id_seq")
    @SequenceGenerator(name = "worker_id_seq", sequenceName = "worker_id_seq")
    Long id;

    String name;
}
