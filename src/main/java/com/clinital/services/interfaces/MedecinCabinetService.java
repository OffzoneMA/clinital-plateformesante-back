package com.clinital.services.interfaces;

import java.util.List;

import com.clinital.dto.CabinetMedecinDTO;
import com.clinital.models.Cabinet;
import com.clinital.models.CabinetMedecinsSpace;
import com.clinital.models.Medecin;
import com.clinital.payload.request.CabinetMedecinsSpaceRequest;

public interface MedecinCabinetService {
    
     public CabinetMedecinsSpace addCabinetMedecinsSpace(CabinetMedecinsSpaceRequest medecinNetwor,Cabinet cabinet,Medecin medecin) throws Exception;
    
    public CabinetMedecinsSpace updateCabinetMedecinsSpace(CabinetMedecinDTO CabinetMedecinDTO) throws Exception;
    
    public void deleteCabinetMedecinsSpace(Long id_cabinet) throws Exception;
    
    public List<?> getAllCabinetMedecinsSpacebyIdMed(Long id_medecin) throws Exception;
    
    public CabinetMedecinsSpace getCabinetMedecinsSpaceByIdMedandIdCab(Long id_medecin, Long id_cabinet) throws Exception;

    public CabinetMedecinsSpace FindMedecinsNetworkByID(Long id_medecin, Long id_follower) throws Exception;
    

}
