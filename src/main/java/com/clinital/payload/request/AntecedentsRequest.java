package com.clinital.payload.request;

import java.time.LocalDate;

import com.clinital.enums.AntecedentTypeEnum;

import lombok.Data;

@Data
public class AntecedentsRequest {
    
    private Long id_anticedent;

    private AntecedentTypeEnum type;

    private String descreption;

    private LocalDate date;
	
    private Long dossier;
}
