package com.clinital.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "pays")
@Data
public class Pays {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_pays;

	private String nom_pays;

	public Pays(@NotBlank String nom_pays) {
		super();
		this.nom_pays = nom_pays;
	}

	public Pays() {
		super();
	}

}
