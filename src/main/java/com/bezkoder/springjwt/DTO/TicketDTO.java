package com.bezkoder.springjwt.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class TicketDTO implements Serializable {

    private String fullName;

    private String cvUrl;

    private String status;

    private String priority;

    private LocalDate start;

    private LocalDate deadline;

    private List<TicketUserDTO>  PIC;

    private List<TicketDepDTO>  listDeps;

    private List<JobLevelDTO>  listJobLevelRelation;

}
