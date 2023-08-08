package com.clinital.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DocumentDTO {
	private Long id_doc;
	@NotNull
	private Long numero_doc;
	@NotBlank
	private String titre_doc;
	@NotNull
	private Date date_ajout_doc;
	@NotBlank
	private String auteur;
	private String fichier_doc;
	private PatientDTO patient;
	private DossierMedicalDTO dossier;
	private List<MedecinDTO> medecins;
	private Boolean archived;

}
