package com.clinital.payload.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ConsultationRequest {

    private Long id_consultation;
	private String Details;
	private LocalDate date;
	private Long medecin;
	private Long patient;
	private Long Rendezvous;
    
}

