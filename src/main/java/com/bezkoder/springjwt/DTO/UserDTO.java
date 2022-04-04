package com.bezkoder.springjwt.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private String username;

    private String fullName;

    private String depName;

    private String role;

    private LocalDateTime createdAt;

    private String createBy;

    private String password;




}
