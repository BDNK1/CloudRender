package tech.theraven.analysis.domain.generic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity implements DatabaseEntity {

    @EqualsAndHashCode.Exclude
    @Column(name = "created_on", updatable = false)
    @CreatedDate
    private Instant createdOn;

    @Version
    @EqualsAndHashCode.Exclude
    @Column(name = "last_modified_on")
    @LastModifiedDate
    private Instant lastModifiedOn;

}
