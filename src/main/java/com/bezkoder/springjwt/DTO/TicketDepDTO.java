package com.bezkoder.springjwt.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TicketDepDTO implements Serializable {
    private DepDTO depEntity;
}
