package com.clinital.payload.response;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.clinital.enums.ModeConsultationEnum;
import com.clinital.enums.MotifConsultationEnum;
import com.clinital.enums.RdvStatutEnum;
import com.clinital.models.Medecin;
import com.clinital.models.ModeConsultation;
import com.clinital.models.Patient;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RendezvousResponse {
	private Long id;
	@JsonProperty("day")
	@NotNull
	private String day;
	private LocalDateTime start;
	private LocalDateTime end;
	private LocalDateTime canceledat;
	private RdvStatutEnum statut;
    private ModeConsultation modeconsultation;
	private MotifConsultationEnum motif;
	private Long medecinid;
	private Long patientid;
	private String LinkVideoCall;


}
