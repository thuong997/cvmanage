package com.bezkoder.springjwt.DTO;

import com.bezkoder.springjwt.models.JobEntity;
import com.bezkoder.springjwt.models.LevelEntity;
import com.bezkoder.springjwt.models.TicketEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class JobLevelDTO {

    private JobDTO jobEntity;

    private LevelDTO levelEntity;

}
