package com.clinital.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clinital.enums.CiviliteEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

/**
 * 
 */
@Data 
public class MedecinDTO {

	private Long id;

	@NotBlank
	private String matricule_med;
	@NotBlank
	private String nom_med;
	@NotBlank
	private String prenom_med;
	@NotBlank
	private String photo_med;
	@NotNull
	private List<ExpertisesMedecinDto> expertisesMedecin;
	@NotNull
	private List<DiplomeMedecinDTO> diplome_med;
	@NotNull
	private List<ExperienceMedecinDTO> experience_med;
	@NotNull
	private String description_med;
	private CiviliteEnum civilite_med;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private VilleDTO ville;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private SpecialiteDTO specialite;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<RendezvousDTO> lesrdvs;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private CabinetDTO cabinet;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<MoyenPaiementDTO> moyenPaiement;

	@NotNull
	private UserDTO user;

	 
}
