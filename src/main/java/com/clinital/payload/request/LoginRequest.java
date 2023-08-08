package com.clinital.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequest {
	@NotBlank(message = "{validation.mail.notEmpty}")
	private String email;

	@NotBlank(message = "{validation.password.notEmpty}")
	private String password;

}
