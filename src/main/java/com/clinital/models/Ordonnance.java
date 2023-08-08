package com.clinital.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ordonnance")
@Data
public class Ordonnance{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ordon;
    private LocalDate Date;
    private String details;

    @ManyToOne
    @JoinColumn(name = "medecin", nullable = true, referencedColumnName = "id", insertable = true, updatable = true)
    private Medecin medecin;

    @ManyToOne
    @JoinColumn(name = "dossier", nullable = true, referencedColumnName = "id_dossier", insertable = true, updatable = true)
    private DossierMedical dossier;

    @OneToOne(optional = true)
    @JoinColumn(name = "rdv_id", referencedColumnName= "id")
    private Rendezvous rendezvous;

    public Ordonnance(){
        super();
    }
    public Ordonnance(String details,Medecin med,DossierMedical dossier,Rendezvous rendezvous){
        super();
        this.details=details;
        this.medecin=med;
        this.dossier=dossier;
        this.rendezvous=rendezvous;
    }

}