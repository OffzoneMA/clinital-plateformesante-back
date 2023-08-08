package com.clinital.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "villes")
@Data
public class Ville {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_ville;

	private String nom_ville;

	@ManyToOne(fetch = FetchType.EAGER)
	private Pays pays;

//	@OneToMany
//	private List<Patient> patients;
//
//	@OneToMany
//	private List<Medecin> medecins;

	public Ville() {
		super();
	}

	public Ville(@NotBlank String nom_ville, Pays pays, List<Patient> patients, List<Medecin> medecins) {
		super();
		this.nom_ville = nom_ville;
		this.pays = pays;

	}

	public Ville(@NotBlank String nom_ville, Pays pays) {
		super();
		this.nom_ville = nom_ville;
		this.pays = pays;
	}

}
