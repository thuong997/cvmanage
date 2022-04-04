package com.bezkoder.springjwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@IdClass(TicketUserId.class)
@Table(name = "ticket_user")
@AllArgsConstructor
@NoArgsConstructor
public class TicketUserRelationEntity extends BaseModel {

    @Id
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticketEntity;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
