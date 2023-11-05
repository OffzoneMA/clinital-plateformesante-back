package com.clinital.dto;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.clinital.enums.MotifConsultationEnum;

import lombok.Data;

@Data
public class MotifConsultationDTO {

  
    private Long id_motif;
	@Enumerated(EnumType.STRING)
	private MotifConsultationEnum motif;

}
