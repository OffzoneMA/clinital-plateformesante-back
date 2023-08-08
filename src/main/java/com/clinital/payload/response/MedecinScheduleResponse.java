package com.clinital.payload.response;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import com.clinital.enums.ConsultationPeriodEnum;

import lombok.Data;

@Data
public class MedecinScheduleResponse {

    private Long id;
    private DayOfWeek day;
    private LocalDateTime availabilityStart;

    private LocalDateTime availabilityEnd;

    private ConsultationPeriodEnum period;

    private long idMedecin;

}
