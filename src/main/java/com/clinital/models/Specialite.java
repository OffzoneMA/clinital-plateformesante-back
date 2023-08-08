package com.clinital.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "specialites")
@Data
public class Specialite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_spec;

	private String libelle;


	@OneToMany(mappedBy="specialite")
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
    private List<Medecin> medecins;

	public Specialite(Long id_spec, String libelle, List<Medecin> medecins) {
		this.id_spec = id_spec;
		this.libelle = libelle;
		this.medecins = medecins;
	}

	public Specialite() {
		super();
	}

	

}
