package com.clinital.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.clinital.dto.MedecinDTO;
import com.clinital.enums.ConsultationPeriodEnum;

import lombok.Data;

@Entity
@Table(name = "type_consultation")
@Data
public class TypeConsultation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consultation_id")
	private Long consultationId;

	@Column(name = "title")
	private String title;

	@Column(name = "tarif")
	private double tarif;

	@ManyToOne
	@JoinColumn(name = "medecin_id", referencedColumnName= "id")
	private Medecin medecin;

	public TypeConsultation(){
		super();
	}



}
