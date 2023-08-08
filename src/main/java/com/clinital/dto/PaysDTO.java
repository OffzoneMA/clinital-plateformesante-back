package com.clinital.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PaysDTO {
	private Long id_pays;
	@NotBlank
	private String nom_pays;

}
