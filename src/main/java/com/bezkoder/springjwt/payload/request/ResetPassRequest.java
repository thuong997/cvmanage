package com.bezkoder.springjwt.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassRequest {
    @NotBlank(message = "Chưa nhập Password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$", message = "Sai định dạng")
    private String newPass;
    @NotBlank(message = "Chưa nhập Password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$", message = "Sai định dạng")
    private String confirmPass;
}
