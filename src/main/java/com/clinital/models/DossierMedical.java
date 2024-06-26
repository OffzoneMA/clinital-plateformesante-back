package com.clinital.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.clinital.enums.PatientTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "dossiers")
@Data
public class DossierMedical {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_dossier;
	private String numDossier;
	private boolean traitement;
	@Enumerated(value = EnumType.STRING)
	@Column(name = "dossier_type")
	private PatientTypeEnum dossierType;
	private String accesscode;
	private boolean fumeur;
	private boolean alchole;
	
	@OneToMany(mappedBy = "dossier")
	@JsonIgnore
	private List<Document> documents;

	@OneToMany(mappedBy = "dossier")
	@JsonIgnore
	private List<Ordonnance> Ordonnance;

	@OneToMany(mappedBy = "dossier")
	@JsonIgnore
	private List<Consultation> consulations;

	@OneToMany(mappedBy = "dossier")
	@JsonIgnore
	private List<Antecedents> antecedents;

	@OneToMany(mappedBy = "dossier")
	@JsonIgnore
	private List<Allergies>  allergies;
	
	@ManyToMany(fetch = FetchType.LAZY,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
      },
      mappedBy = "Meddossiers")
  	@JsonIgnore
  	private List<Medecin> medecins;
	
	
	public DossierMedical() {
		super();
	}

	public DossierMedical(String numDossier, boolean traitement,PatientTypeEnum dossierType, List<Document> documents,List<Medecin> medecins,List<Consultation> consulations,List<Antecedents> antecedents,List<Ordonnance> ordonnance,List<Allergies>  allergies,boolean fumeur,boolean alchole,String accesscode) {
		super();
		this.numDossier = numDossier;
		this.traitement = traitement;
		this.dossierType=dossierType;
		this.fumeur=fumeur;
		this.alchole=alchole;
		this.accesscode=accesscode;
		this.documents = documents;
		this.medecins=medecins;
		this.Ordonnance=ordonnance;
		this.allergies=allergies;
		this.consulations=consulations;
		this.antecedents=antecedents;
		
	}

}
