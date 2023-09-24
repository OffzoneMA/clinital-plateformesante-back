package com.clinital.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class SharingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = true, referencedColumnName = "id", insertable = true, updatable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_patient", nullable = true, referencedColumnName = "id_dossier", insertable = true, updatable = true)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_med", nullable = true, referencedColumnName = "id", insertable = true, updatable = true)
    private Medecin medecin;

    @ManyToOne
    @JoinColumn(name = "id_doc", nullable = true, referencedColumnName = "id_doc", insertable = true, updatable = true)
    private Document document;

    private LocalDateTime dateshare;

    // Constructors, getters, and setters
    public SharingHistory(User user,Medecin medecin,Patient patient,Document document,LocalDateTime dateshare ){
       this.user=user;
       this.patient=patient;
       this.medecin=medecin;
       this.document=document;
       this.dateshare=dateshare;
    }
}
