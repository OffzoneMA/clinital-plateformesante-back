package com.clinital.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

import com.clinital.enums.ConsultationPeriodEnum;
import com.clinital.models.ModeConsultation;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MedecinScheduleDto implements Serializable {

    
    private  DayOfWeek day;

    
    private  LocalDateTime availabilityStart;

   
    private  LocalDateTime availabilityEnd;

    private ModeConsultationDTO Mode;

    private  ConsultationPeriodEnum periode;

    private MedecinDTO medecin;

    private Long cabinet_id;

    public MedecinScheduleDto(){
        super();
    }
}
