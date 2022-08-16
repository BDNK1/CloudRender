package tech.theraven.cloudrender.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "job_id_seq")
    Long id;
    String fileUrl;
    @Embedded
    AnalysisInfo analysis;
    @Embedded
    JobSpecs specs;
    @Enumerated(EnumType.STRING)
    JobStatus status;

    @OneToMany(mappedBy = "job")
    List<WorkUnit> workUnits;

    public boolean isAnalized() {
        return analysis != null;
    }

    public String getName() {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

//    public boolean
}



