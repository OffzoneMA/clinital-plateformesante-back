package com.clinital.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "experience_medecin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceMedecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom_experience;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_debut;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_fin;


    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;
}
