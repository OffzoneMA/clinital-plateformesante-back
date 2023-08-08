package com.clinital.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;


import lombok.Data;

@Data
public class CabinetDTO {
	private Long id_cabinet;
	@NotNull
	private String nom;
	@NotNull
	private String adresse;
	@NotNull
	private String code_post;
	@NotNull
	private Date horaires;
	private String phoneNumber;
	//private List<SecretaireDTO> secretaires;
	private List<MedecinDTO> medecins;

}
