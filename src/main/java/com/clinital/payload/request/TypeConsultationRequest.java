package com.clinital.payload.request;

import com.clinital.enums.ConsultationPeriodEnum;

import lombok.Data;

@Data
public class TypeConsultationRequest {

	private String title;
	private double tarif;
	private Long medecinId;

}
