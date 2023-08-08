package com.clinital.services.interfaces;

import java.util.List;
import java.util.Map;

import com.clinital.dto.TypeConsultationDTO;
import com.clinital.models.TypeConsultation;
import com.clinital.payload.request.TypeConsultationRequest;

public interface TypeConsultationServices {

    public TypeConsultation addTypeConsultation(TypeConsultationRequest TypeConsultation) throws Exception;
    
    public TypeConsultationDTO updateTypeConsultation(TypeConsultationRequest TypeConsultation,long id_consultation) throws Exception;
    
    public void deleteTypeConsultation(Long id_consultation ,long id_medecin) throws Exception;
    
    public List<TypeConsultation> findAllTypeConsultationByMedecinId(Long id_medecin) throws Exception;
    
    public TypeConsultation findTypeConsultationById(Long id_consultation) throws Exception;
    
}
