package com.clinital.payload.response;

import com.clinital.enums.ConsultationPeriodEnum;

import lombok.Data;

@Data
public class TypeConsultationResponse {

	private Long consultationId;
	private String title;
	private double tarif;
	private Long medecinId;

}
