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
import com.clinital.enums.MotifConsultationEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "motifs_consultation")
@Data
public class MotifConsultation {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_motif;
    @Column(name = "libelle")
	@Enumerated(EnumType.STRING)
	private MotifConsultationEnum motif;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "motifConsultation")
    @JsonIgnore
  	private List<MedecinSchedule> Schedules;
	
	public MotifConsultation() {
		super();
	}

	public MotifConsultation(MotifConsultationEnum motif) {
		super();
		this.motif = motif;
	}
}



    

