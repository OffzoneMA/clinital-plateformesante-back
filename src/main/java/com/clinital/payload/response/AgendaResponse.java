package com.clinital.payload.response;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clinital.enums.ConsultationPeriodEnum;
import com.clinital.enums.ModeConsultationEnum;
import com.clinital.models.MedecinSchedule;
import com.clinital.models.ModeConsultation;
import com.clinital.models.MotifConsultation;

import lombok.Data;

@Data
public class AgendaResponse {

	DayOfWeek day;
	LocalDateTime canceledAt;
	ConsultationPeriodEnum period;
	List<ModeConsultation> modeconsultation;
	List<MotifConsultation> motifconsultation;
	Boolean isnewpatient;
	LocalDateTime workingDate;
	long week;
	List<GeneralResponse> medecinTimeTable = new ArrayList<GeneralResponse>();
	List<HorairesResponse> workingHours = new ArrayList<HorairesResponse>();

	List<String> availableSlot = new ArrayList<String>();

	public AgendaResponse(DayOfWeek day, List<String> availableSlot,LocalDateTime work) {
		this.day = day;
		this.availableSlot = availableSlot;
		this.workingDate=work;
	}

	public AgendaResponse() {
	}

}
