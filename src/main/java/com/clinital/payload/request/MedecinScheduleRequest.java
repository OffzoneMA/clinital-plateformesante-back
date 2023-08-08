package com.clinital.payload.request;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.clinital.controllers.MedecinController.MedecinParams;
import com.clinital.enums.ConsultationPeriodEnum;
import com.clinital.enums.ModeConsultationEnum;
import com.clinital.models.Medecin;
import com.clinital.models.ModeConsultation;
import com.clinital.models.MotifConsultation;
import com.clinital.models.TypeConsultation;

import lombok.Data;

@Data
public class MedecinScheduleRequest {

	private Long id;
	private String day;
	private LocalDateTime availabilityStart;
	private LocalDateTime availabilityEnd;
	private ConsultationPeriodEnum period;
	private List<ModeConsultation> modeconsultation;
	private List<MotifConsultation> motifconsultation;
	private Long medecin_id;
	private Long cabinet_id;
	private Boolean isnewpatient;
}
