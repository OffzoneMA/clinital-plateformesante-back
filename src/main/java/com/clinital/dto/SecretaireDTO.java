package com.clinital.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SecretaireDTO{
	private Long id;
	@NotNull
	private String nom;
	@NotNull
	private String prenom;
	@NotNull
	private Date dateNaissance;
	@NotNull
	private String adresse;
	@NotNull
	private CabinetDTO cabinet;
	@NotNull
	private UserDTO user;
	
	
}
