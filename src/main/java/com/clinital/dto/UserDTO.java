package com.clinital.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.clinital.enums.ERole;
import com.clinital.enums.ProviderEnum;
import com.clinital.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class UserDTO {

	private Long id;
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	@Size(max = 120)
	@JsonProperty(access = Access.WRITE_ONLY)
	//private String password;
	private String telephone;


}
