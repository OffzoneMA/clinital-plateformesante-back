package com.clinital.payload.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.clinital.models.Cabinet;
import com.clinital.models.User;

import lombok.Data;

@Data
public class SecritaireRequest {

	@NotNull
	private String nom;
	@NotNull
	private String prenom;
	@NotNull
	private Date dateNaissance;
	@NotNull
	private String adresse;
	@NotNull
	private Long cabinetid;
	
}
