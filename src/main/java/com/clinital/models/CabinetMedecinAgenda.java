//package com.clinital.models;
//
//import java.time.LocalDateTime;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import org.checkerframework.checker.units.qual.Time;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import lombok.Data;
//
//@Entity
//@Table(name = "cabinet_medecin_agenda")
//@Data
//public class CabinetMedecinAgenda {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long agendaId;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Cabinet cabinet;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	private TypeConsultation typeConsultation;
//
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private LocalDateTime day;
//
//	@Time
//	private LocalDateTime start;
//
//	@Time
//	private LocalDateTime end;
//
//}
