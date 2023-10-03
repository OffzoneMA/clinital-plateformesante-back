package com.clinital.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.clinital.dto.SharingHistoryDTO;
import com.clinital.models.Document;
import com.clinital.models.Medecin;
import com.clinital.models.Patient;
import com.clinital.models.SharingHistory;
import com.clinital.models.User;
import com.clinital.payload.request.SharingHistoryRequest;
import com.clinital.repository.DocumentRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.PatientRepository;
import com.clinital.repository.UserRepository;
import com.clinital.repository.sharingHistoryrepository;
import com.clinital.services.interfaces.SharingHistoryService;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;

@Service
public class SharingHistoryServiceImpl implements SharingHistoryService {
    @Autowired
    public MedecinRepository medecinRepository;
    @Autowired
    private sharingHistoryrepository sharinghistoryRepository ;
    @Autowired
    public UserRepository userrepo;
    @Autowired
    public PatientRepository patientrepo;
    @Autowired
    public DocumentRepository docrepo;
    @Autowired
    GlobalVariables globalVariables;
    @Autowired
    public ClinitalModelMapper mapper;


    @Override
    public SharingHistory addSharingHistory(SharingHistoryRequest SharingHistory) throws Exception {
       try {
         // TODO Auto-generated method stub
         Medecin medecin=medecinRepository.findById(SharingHistory.getMedecin()).orElseThrow(()->new Exception("No Medecin exist with this id"));
         Patient patient=patientrepo.findById(SharingHistory.getPatient()).orElseThrow(()->new Exception("No Medecin exist with this id"));
         Document document=docrepo.findById(SharingHistory.getDocument()).orElseThrow(()->new Exception("No Medecin exist with this id"));
         User currentUser=globalVariables.getConnectedUser();
         SharingHistory newshare=new SharingHistory(currentUser, medecin, patient, document, SharingHistory.getSharingdate());
         sharinghistoryRepository.save(newshare);
         return newshare;
       } catch (Exception e) {
        // TODO: handle exception
        throw new Exception(e.getMessage());
       }
        
    }

    @Override
    public ResponseEntity<SharingHistoryDTO> updateSharingHistory(SharingHistoryRequest SharingHistory)
            throws Exception {
          try {
         // TODO Auto-generated method stub
          SharingHistory shareHistory=sharinghistoryRepository.findById(SharingHistory.getId()).orElseThrow(()->new Exception("No sharing exist with this id"));
          Medecin medecin=medecinRepository.findById(SharingHistory.getMedecin()).orElseThrow(()->new Exception("No Medecin exist with this id"));
         Patient patient=patientrepo.findById(SharingHistory.getPatient()).orElseThrow(()->new Exception("No Patient exist with this id"));
         Document document=docrepo.findById(SharingHistory.getDocument()).orElseThrow(()->new Exception("No Document exist with this id"));
         User currentUser=globalVariables.getConnectedUser();
         shareHistory.setMedecin(medecin);
         shareHistory.setPatient(patient);
         shareHistory.setDocument(document);
         shareHistory.setUser(currentUser);
         shareHistory.setDateshare(SharingHistory.getSharingdate());
         sharinghistoryRepository.save(shareHistory);
         return ResponseEntity.ok(mapper.map(shareHistory,SharingHistoryDTO.class));

       } catch (Exception e) {
        // TODO: handle exception
        throw new Exception(e.getMessage());
       }
        
        
    }

    @Override
    public String deleteSharingHistory(Long id_share) throws Exception {
          try {
         SharingHistory shareHistory=sharinghistoryRepository.findById(id_share).orElseThrow(()->new Exception("No sharing exist with this id"));
         if(sharingHistoryrepository.Deletsharehstory(shareHistory.getId())){
          return "record has been deleted seccessfully";
         }else{
          return "somthing wen wrong !!";
         }
         
       } catch (Exception e) {
        // TODO: handle exception
        throw new Exception(e.getMessage());
       }
        
        
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByMedecinIdAndPatientId(Long id_medecin, Long id_patient)
        throws Exception {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByMedecinIdAndPatientId'");
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByPatientId(Long id_patient) throws Exception {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByPatientId'");
    }

    @Override
    public List<Medecin> findAllSharingHistoryByMedecinId(Long id_med) throws Exception {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findAllSharingMedecinByPatientId'");
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByMedecinIdAndUserId(Long id_medecin, Long id_User)
        throws Exception {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByMedecinIdAndUserId'");
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByUserId(Long user_id) throws Exception {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByUserId'");
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByDocId(Long id_document) throws Exception {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByDocId'");
    }

    @Override
    public SharingHistory findSharingHistoryById(Long id_share) throws Exception {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findSharingHistoryById'");
    }

//     @Override
//     public SharingHistory findSharingHistoryById(Long id_consultation) throws Exception {
//          try {
//          // TODO Auto-generated method stub
//        } catch (Exception e) {
//         // TODO: handle exception
//         throw new Exception(e.getMessage());
//        }
        

//     }

//     @Override
//     public List<SharingHistory> findAllSharingHistoryByMedecinIdAndPatientId(Long id_medecin, Long id_patient)
//         throws Exception {
//       // TODO Auto-generated method stub
//       throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByMedecinIdAndPatientId'");
//     }

//     @Override
//     public List<SharingHistory> findAllSharingHistoryByMedecinIdAndUserId(Long id_medecin, Long id_User)
//         throws Exception {
//       // TODO Auto-generated method stub
//       throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByMedecinIdAndUserId'");
//     }

//     @Override
//     public List<SharingHistory> findAllSharingHistoryByDocId(Long id_document) throws Exception {
//       // TODO Auto-generated method stub
//       throw new UnsupportedOperationException("Unimplemented method 'findAllSharingHistoryByDocId'");
//     }


    
 }
