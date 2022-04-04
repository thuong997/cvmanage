package com.bezkoder.springjwt.payload.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class LoginRequest {
	@NotBlank(message = "Chưa nhập Email")
	@Email(regexp = "^([a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]){1,64}@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message = "Sai định dạng")
	private String email;

	@NotBlank(message = "Chưa nhập Password")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$", message = "Sai định dạng")
	private String password;
}
