package com.bezkoder.springjwt.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "levels", uniqueConstraints = {
        @UniqueConstraint(columnNames = "level_name")
})
public class LevelEntity extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private int levelId;

    @Column(name ="level_name", length = 100, unique = true)
    @NotNull
    private String levelName;

    @OneToMany(mappedBy = "levelEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JobLevelRelationEntity> jobLevelRelationEntities;

    public LevelEntity(String levelName) {
        this.levelName = levelName;
    }

    public LevelEntity() {

    }
}
