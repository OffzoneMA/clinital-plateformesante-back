package com.clinital.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.clinital.enums.AllergiesTypeEnum;

import lombok.Data;

@Entity
@Table(name = "allergies")
@Data
public class Allergies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    //private AllergiesTypeEnum type;
    @ManyToOne(cascade = CascadeType.ALL)
	private DossierMedical dossier;
    public Allergies(){
        super();
    }

    public Allergies(String name){
        super();
        this.name=name;
    }
}
