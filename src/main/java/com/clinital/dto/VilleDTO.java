package com.clinital.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class VilleDTO {
	private Long id_ville;
	@NotBlank
	private String nom_ville;
	private PaysDTO pays;

}
