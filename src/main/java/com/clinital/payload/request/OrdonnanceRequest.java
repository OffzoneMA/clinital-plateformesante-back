package com.clinital.payload.request;

import java.time.LocalDate;

import com.clinital.dto.DossierMedicalDTO;
import com.clinital.dto.MedecinDTO;

import lombok.Data;
@Data
public class OrdonnanceRequest {

    private Long id_ordonnance;
	private String Details;
	private LocalDate date;
	private Long medecin;
	private Long dossier;
	private Long rendezvous;
    
}
