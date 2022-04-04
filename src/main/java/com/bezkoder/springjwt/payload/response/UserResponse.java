package com.bezkoder.springjwt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {

    private String email;

    private String fullName;

    private String depName;

    private String role;

    private LocalDateTime createdAt;

    private String createBy;


}
