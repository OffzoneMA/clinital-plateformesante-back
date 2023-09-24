package com.clinital.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.clinital.dto.SharingHistoryDTO;
import com.clinital.models.SharingHistory;
import com.clinital.payload.request.SharingHistoryRequest;

public interface SharingHistoryService {
    
    public SharingHistory addSharingHistory(SharingHistoryRequest SharingHistory) throws Exception;
    
    public ResponseEntity<SharingHistoryDTO> updateSharingHistory(SharingHistoryRequest SharingHistory) throws Exception;
    
    // public void deleteSharingHistory(Long id_share) throws Exception;
    
    // public List<SharingHistory> findAllSharingHistoryByMedecinIdAndPatientId(Long id_medecin,Long id_patient) throws Exception;
    // public List<SharingHistory> findAllSharingHistoryByMedecinIdAndUserId(Long id_medecin,Long id_User) throws Exception;
    // public List<SharingHistory> findAllSharingHistoryByDocId(Long id_document) throws Exception;
    
    // public SharingHistory findSharingHistoryById(Long id_share) throws Exception;

}
