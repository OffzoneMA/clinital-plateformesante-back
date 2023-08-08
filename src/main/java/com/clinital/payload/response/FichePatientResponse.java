package com.clinital.payload.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.clinital.dto.VilleDTO;
import com.clinital.enums.CiviliteEnum;
import com.clinital.models.Antecedents;
import com.clinital.models.Document;
import com.clinital.models.Rendezvous;

import lombok.Data;

@Data
public class FichePatientResponse {
    
    private Long id;

	private String nom_pat;
	private String prenom_pat;
	private Date dateNaissance;
	private String adresse_pat;
	private String codePost_pat;
	private String matricule_pat;
	private CiviliteEnum civilite_pat;
	private VilleDTO ville;
	private String placeOfBirth;
	private String mutuelNumber;
	private String patientEmail;
	private String patientTelephone;
    private List<Rendezvous> allrdv= new ArrayList<Rendezvous>();
    private List<Document> alldoc = new ArrayList<Document>();
	private List<Antecedents> Allantecedents = new ArrayList<Antecedents>();
}
