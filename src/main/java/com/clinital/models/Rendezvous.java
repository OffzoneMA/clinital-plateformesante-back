package com.clinital.models;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

import com.clinital.enums.ModeConsultationEnum;
import com.clinital.enums.MotifConsultationEnum;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import com.clinital.enums.RdvStatutEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "rendezvous")
@Data
public class Rendezvous {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "day")
	@Enumerated(value = EnumType.ORDINAL)
	private DayOfWeek day;
//	private String motif;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime start;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime end;
	@Nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime canceledAt;

	@Enumerated(EnumType.STRING)
	private RdvStatutEnum statut;
	private Boolean iSnewPatient;
	private String Commantaire;
	private String LinkVideoCall;
	// @Enumerated(EnumType.STRING)
	// private ModeConsultationEnum modeConsultation;

	//@Enumerated(EnumType.STRING)
	// private List<MotifConsultationEnum> motif=new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "medecin", nullable = false, referencedColumnName = "id", insertable = true, updatable = true)
	private Medecin medecin;

	@ManyToOne
	@JoinColumn(name = "patient", nullable = true, referencedColumnName = "id", insertable = true, updatable = true)
	private Patient patient;

	// @ManyToOne
	// @JoinColumn(name = "type_consultation_id", nullable = false, referencedColumnName = "consultation_id", insertable = true, updatable = true)
	// private TypeConsultation typeConsultation;

	@OneToMany(mappedBy = "rendezvous")
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Document> documents;

	// @ManyToOne
	// @JoinColumn(name = "medecin_schedule_id", nullable = false, referencedColumnName = "id", insertable = true, updatable = true)
	// private MedecinSchedule medecinSchedule;

	@OneToOne(cascade = CascadeType.ALL)

	@JoinColumn(name = "id_mode", referencedColumnName= "id_mode")
	private ModeConsultation modeConsultation;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_motif", referencedColumnName= "id_motif")
	private MotifConsultation motifConsultation;

	@ManyToOne
	@JoinColumn(name = "cabinet", nullable = true, referencedColumnName = "id_cabinet", insertable = true, updatable = true)
	private Cabinet cabinet;
	public Rendezvous() {
		super();
	}

	public Rendezvous(DayOfWeek day, @NotBlank String motif, LocalDateTime start, LocalDateTime end,
			LocalDateTime canceledAt, RdvStatutEnum statut,MotifConsultation motifconsul, Medecin medecin,Boolean iSnewPatient, Patient patient,ModeConsultation modeConsultation,String Commantaire,String LinkVideoCall,Cabinet cabinet) {
		super();
		this.day = day;
		this.start = start;
		this.end = end;
		this.canceledAt = canceledAt;
		this.statut = statut;
		this.medecin = medecin;
		this.patient = patient;
		this.motifConsultation= motifconsul;
		this.modeConsultation=modeConsultation;
		this.iSnewPatient=iSnewPatient;
		this.Commantaire=Commantaire;
		this.LinkVideoCall=LinkVideoCall;
		this.cabinet=cabinet;
	}

}
