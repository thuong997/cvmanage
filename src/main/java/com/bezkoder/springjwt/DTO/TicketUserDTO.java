package com.bezkoder.springjwt.DTO;

import com.bezkoder.springjwt.payload.response.UserDetailInTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TicketUserDTO implements Serializable {

    private UserDetailInTicket userEntity;
}
