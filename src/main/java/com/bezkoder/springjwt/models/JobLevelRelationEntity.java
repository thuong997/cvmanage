package com.bezkoder.springjwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@IdClass(JobLevelId.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_level")
public class JobLevelRelationEntity extends BaseModel{

    @Id
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticketEntity;

    @Id
    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobEntity jobEntity;

    @Id
    @ManyToOne
    @JoinColumn(name = "level_id")
    private LevelEntity levelEntity;

}
