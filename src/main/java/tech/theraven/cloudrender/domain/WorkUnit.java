package tech.theraven.cloudrender.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class WorkUnit extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    Job job;

    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToOne
    Worker worker;


}
