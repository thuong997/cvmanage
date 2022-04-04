package com.bezkoder.springjwt.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "jobs", uniqueConstraints = {
        @UniqueConstraint(columnNames = "job_name")
})
public class JobEntity extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int jobId;

    @Column(name ="job_name", length = 100, nullable = false, unique = true)
    @NotNull
    private String jobName;

    @OneToMany(mappedBy = "jobEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JobLevelRelationEntity> jobLevelRelationEntities;

    public JobEntity(String jobName) {
        this.jobName = jobName;
    }

    public JobEntity() {

    }
}
