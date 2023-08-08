package com.clinital.payload.response;

import java.util.Date;

import com.clinital.dto.VilleDTO;
import com.clinital.enums.CiviliteEnum;
import com.clinital.enums.PatientTypeEnum;

import lombok.Data;

@Data
public class PatientResponse {

	private Long id;

	private String nom_pat;
	private String prenom_pat;
	private Date dateNaissance;
	private String adresse_pat;
	private String codePost_pat;
	private String matricule_pat;
	private CiviliteEnum civilite_pat;
	private VilleDTO ville;
//	private UserDTO user;
	private String placeOfBirth;
	private String mutuelNumber;
	private String patientEmail;
	private String patientTelephone;
	private PatientTypeEnum patient_type;

}
