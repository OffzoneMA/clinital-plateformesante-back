package com.clinital.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "expertises")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpertisesMedecin {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom_exp;

    @ManyToMany
    private List<Medecin> medecins;

}
