package com.clinital.models;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ValueGenerationType;

import com.clinital.enums.AntecedentTypeEnum;

import lombok.Data;

@Entity
@Table(name = "anticedents")
@Data
public class Antecedents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_anticedent;

    private AntecedentTypeEnum type;

    private String descreption;

    private LocalDate date;

    @ManyToOne(cascade = CascadeType.ALL)
	private DossierMedical dossier;
    
}
