package com.clinital.models;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.clinital.enums.CabinetDocStateEnum;
import com.clinital.enums.CabinetDocuemtsEnum;

import lombok.Data;

@Entity
@Table(name="DocumentsCabinet")
@Data
public class DocumentsCabinet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private CabinetDocuemtsEnum type_doc;
    private LocalDate date_ajout_doc;
	private String fichier_doc;
    
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cabinet", nullable = false, referencedColumnName = "id_cabinet", insertable = true, updatable = true)
    private Cabinet cabinet;

    private CabinetDocStateEnum validationState;

    public DocumentsCabinet(){
        super();
    }
    public DocumentsCabinet(CabinetDocuemtsEnum type,LocalDate date,String file, Cabinet cabinet,CabinetDocStateEnum state){
        super();
        this.type_doc=type;
        this.cabinet=cabinet;
        this.date_ajout_doc=date;
        this.fichier_doc=file;
        this.validationState=state;
    }
}
