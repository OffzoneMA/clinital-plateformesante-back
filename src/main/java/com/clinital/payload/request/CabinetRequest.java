package com.clinital.payload.request;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.clinital.dto.MedecinDTO;
import com.clinital.models.CabinetMedecinsSpace;

import lombok.Data;

@Data
public class CabinetRequest {
    
    private Long id_cabinet;
	@NotNull
	private String nom;
	@NotNull
	private String adresse;
	@NotNull
	private String code_post;
	@NotNull
	private Long id_ville;
	@NotNull
	private String phoneNumber;
	//private List<SecretaireDTO> secretaires;
	//private List<MedecinDTO> medecins;
    private CabinetMedecinsSpaceRequest cabinetmedecin;

	@NotNull
	private long id_medecin;


}
