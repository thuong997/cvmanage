package com.bezkoder.springjwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@IdClass(TicketDepartmentId.class)
@Table(name = "ticket_department")
@AllArgsConstructor
@NoArgsConstructor
public class TicketDepRelationEntity extends BaseModel{
    @Id
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticketEntity;

    @Id
    @ManyToOne
    @JoinColumn(name = "dep_id")
    private DepEntity depEntity;

}
