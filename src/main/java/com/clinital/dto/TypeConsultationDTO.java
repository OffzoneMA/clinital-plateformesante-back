package com.clinital.dto;

import com.clinital.enums.ConsultationPeriodEnum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TypeConsultationDTO {

	private Long consultationId;

	
	private String title;

	
	private double tarif;

	
	private Long medecinid;

}
