package com.bezkoder.springjwt.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketDetailRequest {
    private int ticketId;
    private int userId;
}
