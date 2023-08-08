package com.clinital.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.clinital.dto.MedecinScheduleDto;
import com.clinital.enums.ModeConsultationEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "mode_consultation")
@Data
public class ModeConsultation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_mode;
    @Column(name = "mode_consultation")
	@Enumerated(EnumType.STRING)
	private ModeConsultationEnum mode;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "modeconsultation")
	@JsonIgnore
  	private List<MedecinSchedule> Schedules;
	
	
	public ModeConsultation() {
		super();
	}

	public ModeConsultation(ModeConsultationEnum mode) {
		super();
		this.mode = mode;
	}

}

    

