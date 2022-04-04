package com.bezkoder.springjwt.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketDepartmentId implements Serializable {

    private int ticketEntity;
    private int depEntity;

}
