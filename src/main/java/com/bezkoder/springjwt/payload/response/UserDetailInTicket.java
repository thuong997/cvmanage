package com.bezkoder.springjwt.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailInTicket {
    private String fullName;
    private String username;
    private int depId;
}
