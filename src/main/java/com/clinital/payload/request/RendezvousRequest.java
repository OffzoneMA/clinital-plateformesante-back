package com.clinital.payload.request;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.clinital.enums.ModeConsultationEnum;
import com.clinital.enums.MotifConsultationEnum;
import com.clinital.enums.RdvStatutEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class RendezvousRequest {

	
	@JsonProperty("day")
	private String day;
	private LocalDateTime start;
	private LocalDateTime end;
	private LocalDateTime canceledat;
	private RdvStatutEnum statut;
    private Long modeconsultation;
	private MotifConsultationEnum motif;
	private Long medecinid;
	private Long patientid;
	

//private Long typeConsultationId;
//	private Long medecinScheduleId;

}
