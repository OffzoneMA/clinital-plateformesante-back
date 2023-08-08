package com.clinital.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "moyenspaiement")
@Data
public class MoyenPaiement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_mp;
	private String type;

	public MoyenPaiement() {
		super();
	}

	public MoyenPaiement(String type) {
		super();
		this.type = type;
	}

}
