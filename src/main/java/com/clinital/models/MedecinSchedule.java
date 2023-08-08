package com.clinital.models;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.clinital.enums.ConsultationPeriodEnum;
import com.clinital.enums.ModeConsultationEnum;

import lombok.Data;

@Data
@Table(name = "medecin_schedule")
@Entity
public class MedecinSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "day")
	@Enumerated(value = EnumType.ORDINAL)
	private DayOfWeek day;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime availabilityStart;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime availabilityEnd;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "modeschedules", 
	joinColumns = { @JoinColumn(name = "sched_id", referencedColumnName   ="id") },
	inverseJoinColumns = { @JoinColumn(name = "mode_id",referencedColumnName="id_mode") })
	private List<ModeConsultation> modeconsultation;

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name = "motif_consultations_schedules",
	 	joinColumns = @JoinColumn(name = "id_sched",referencedColumnName   ="id"),
		inverseJoinColumns = @JoinColumn(name = "id_motif",referencedColumnName   ="id_motif"))
	private List<MotifConsultation> motifConsultation;
	 
	@Column(name = "period")
	@Enumerated(EnumType.STRING)
	private ConsultationPeriodEnum period;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "medecin_id", nullable = false, referencedColumnName = "id", insertable = true, updatable = true)
	private Medecin medecin;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cabinet", nullable = false, referencedColumnName = "id_cabinet", insertable = true, updatable = true)
	private Cabinet cabinet;

	private Boolean isnewpatient;
	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "type_consultation_id", nullable = false, referencedColumnName = "consultation_id", insertable = true, updatable = true)
	// private TypeConsultation typeConsultation;
	public MedecinSchedule(){
		super();
	}
	
}
