package com.bezkoder.springjwt.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class JobLevelId implements Serializable {

    private int ticketEntity;
    private int jobEntity;
    private int levelEntity;
}
