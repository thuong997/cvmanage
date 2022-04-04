package com.bezkoder.springjwt.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket")
public class TicketEntity extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private int ticketId;

    @Column(name = "full_name", length = 50, nullable = false)
    private String fullName;

    @Column(name = "cv_url", length = 500)
    private String cvUrl;

    @Column(name = "`status`", length = 50, nullable = false)
    private String status;

    @Column(name = "priority", length = 50, nullable = false)
    private String priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "`start`")
    private LocalDate start;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "is_changed", length = 1000)
    private String isChanged;

    @OneToMany(mappedBy = "ticketEntity", fetch = FetchType.LAZY)
    private List<TicketDepRelationEntity>  listDeps;

    @OneToMany(mappedBy = "ticketEntity", fetch = FetchType.LAZY)
    private List<TicketUserRelationEntity>  PIC;

    @OneToMany(mappedBy = "ticketEntity", fetch = FetchType.LAZY)
    private List<JobLevelRelationEntity>  jobLevelRelation;

}
