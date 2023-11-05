package com.clinital.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.clinital.enums.ModeConsultationEnum;
import com.clinital.enums.MotifConsultationEnum;
import com.clinital.enums.RdvStatutEnum;
import com.clinital.models.ModeConsultation;
import com.clinital.models.MotifConsultation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;

@Data
public class RendezvousDTO {

	private Long id;
	private DayOfWeek Day;
	// @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
	// @JsonSerialize(using = LocalDateTimeSerializer.class)
	// @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime start;
	// @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
	// @JsonSerialize(using = LocalDateTimeSerializer.class)
	// @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime end;
	private LocalDateTime canceledat;
	@Enumerated(EnumType.STRING)
	private RdvStatutEnum statut;
	private Long modeconsultation;
	private Long medecinid;
	private long patientid;
	private Boolean isnewpatient;
	private String commantaire;
	private Long motif;
	private String LinkVideoCall;
	private Long cabinet;

}
