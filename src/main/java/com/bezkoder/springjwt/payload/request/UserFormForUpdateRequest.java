package com.bezkoder.springjwt.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFormForUpdateRequest implements Serializable {

    @NotBlank(message = "Chưa nhập Email")
    @Email(regexp = "^([a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]){1,64}@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message = "Sai định dạng")
    private String email;

    private String password;

    @NotBlank(message = "Không được để trống")
    private String fullName;

    @NotNull(message = "Không được để trống")
    private int depId;

    @NotBlank(message = "Không được để trống")
    private String role;

    private int userIdLogin;

}
