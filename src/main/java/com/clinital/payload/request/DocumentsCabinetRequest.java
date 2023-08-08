package com.clinital.payload.request;

import java.time.LocalDate;

import com.clinital.enums.CabinetDocStateEnum;
import com.clinital.enums.CabinetDocuemtsEnum;

import lombok.Data;

@Data
public class DocumentsCabinetRequest {

    private Long Id_doccab;
    private CabinetDocuemtsEnum type;
    private LocalDate addDate;
	private String fichier_doc;
    private long id_cabinet;
    private CabinetDocuemtsEnum docstate;
    
}
