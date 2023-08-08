package com.clinital.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class VerifyEmailRequest {

	@NotBlank
	@Email
	private String email;

	private Integer otpNo;

}