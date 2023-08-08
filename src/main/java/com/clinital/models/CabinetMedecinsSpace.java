package com.clinital.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.clinital.enums.CabinetStatuMedcinEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name = "Cabinet_Medecins")
@Data
public class CabinetMedecinsSpace {

    @EmbeddedId
    private CabinetMedecinsIDs id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @MapsId("medecin_id")
    @JoinColumn(name = "medecin_id", referencedColumnName = "id")
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Medecin medecin;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @MapsId("cabinet_id")
    @JoinColumn(name = "cabinet_id", referencedColumnName = "id_cabinet")
    private Cabinet cabinet;

    @Enumerated(EnumType.STRING)
    private CabinetStatuMedcinEnum status;


    public CabinetMedecinsSpace() {
        super();
    }

    public CabinetMedecinsSpace(Medecin medecin, Cabinet cabinet, CabinetStatuMedcinEnum status) {
        super();
        this.id = new CabinetMedecinsIDs(medecin.getId(),cabinet.getId_cabinet());
        this.medecin = medecin;
        this.cabinet = cabinet;
        this.status = status;
    }
    
}
