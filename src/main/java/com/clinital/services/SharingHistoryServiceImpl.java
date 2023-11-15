package com.clinital.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.clinital.dto.SharingHistoryDTO;
import com.clinital.models.Document;
import com.clinital.models.DossierMedical;
import com.clinital.models.Medecin;
import com.clinital.models.Patient;
import com.clinital.models.SharingHistory;
import com.clinital.models.User;
import com.clinital.payload.request.SharingHistoryRequest;
import com.clinital.repository.DocumentRepository;
import com.clinital.repository.DossierMedicalRepository;
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
    public DossierMedicalRepository dossrepo;
    @Autowired
    GlobalVariables globalVariables;
    @Autowired
    public ClinitalModelMapper mapper;


    @Override
    public SharingHistory addSharingHistory(SharingHistoryRequest SharingHistory) throws Exception {
       try {
         // TODO Auto-generated method stub
         Medecin medecin=medecinRepository.findById(SharingHistory.getMedecin()).orElseThrow(()->new Exception("No Medecin exist with this id"));
         Patient patient=patientrepo.findById(SharingHistory.getPatient()).orElseThrow(()->new Exception("No Patient exist with this id"));
         DossierMedical dossierMedical=dossrepo.findById(SharingHistory.getDossier()).orElseThrow(()->new Exception("No Folder exist with this id"));
         User currentUser=globalVariables.getConnectedUser();
         SharingHistory newshare=new SharingHistory(currentUser, medecin, patient, dossierMedical, LocalDateTime.now());
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
         DossierMedical dossierMedical=dossrepo.findById(SharingHistory.getDossier()).orElseThrow(()->new Exception("No Folder exist with this id"));
         User currentUser=globalVariables.getConnectedUser();
         shareHistory.setMedecin(medecin);
         shareHistory.setPatient(patient);
         shareHistory.setDossierMedical(dossierMedical);
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
          try {
          Patient patient=patientrepo.findById(id_patient).orElseThrow(()->new Exception("No matchinf Found for this Patient ID"));
          Medecin medecin=medecinRepository.findById(id_patient).orElseThrow(()->new Exception("No matchinf Found for this Medecin ID"));

          List<SharingHistory> shares=sharinghistoryRepository.findAllSharingHistoryByMedecinIdAndPatientId(medecin.getId(), patient.getId())
          .stream()
          .map(share->mapper.map(share, SharingHistory.class)).collect(Collectors.toList());
          return shares;
          
            
          } catch (Exception e) {
            // TODO: handle exception 
            throw new Exception(e.getMessage());
          }
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByPatientId(Long id_patient) throws Exception {
    try {
          Patient patient=patientrepo.findById(id_patient).orElseThrow(()->new Exception("No matchinf Found for this Patient ID"));
          List<SharingHistory> shares=sharinghistoryRepository.findAllSharingHistoryByPatientId(patient.getId())
          .stream()
          .map(share->mapper.map(share, SharingHistory.class)).collect(Collectors.toList());
          return shares;
            
          } catch (Exception e) {
            // TODO: handle exception 
            throw new Exception(e.getMessage());
          }
    }

    @Override
    public List<Medecin> findAllSharingHistoryByMedecinId(Long id_med,Long id_patient) throws Exception {
        try {
          Patient patient=patientrepo.findById(id_patient).orElseThrow(()->new Exception("No matchinf Found for this Patient ID"));
          Medecin medecin=medecinRepository.findById(id_med).orElseThrow(()->new Exception("No matchinf Found for this Medecin ID"));
          List<Medecin> medecins=sharinghistoryRepository.findAllSharingHistoryByMedecinId(medecin.getId(), patient.getId())
          .stream()
          .map(med->mapper.map(med, Medecin.class)).collect(Collectors.toList());
          return medecins;     
          } catch (Exception e) {
            // TODO: handle exception 
            throw new Exception(e.getMessage());
          }
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByMedecinIdAndUserId(Long id_medecin)
        throws Exception {
     try {
         
          Medecin medecin=medecinRepository.findById(id_medecin).orElseThrow(()->new Exception("No matchinf Found for this Medecin ID"));
         List<SharingHistory> shares=sharinghistoryRepository.findAllSharingHistoryByMedecinIdAndUserId(medecin.getId(),globalVariables.getConnectedUser().getId())
          .stream()
          .map(share->mapper.map(share, SharingHistory.class)).collect(Collectors.toList());
          return shares;     
          } catch (Exception e) {
            // TODO: handle exception 
            throw new Exception(e.getMessage());
          }
     
        }

    @Override
    public ResponseEntity<?> findAllSharingHistoryByUserId(Long user_id) throws Exception {
   try {
         
         
         List<SharingHistory> shares=sharinghistoryRepository.findAllSharingHistoryByUserId(globalVariables.getConnectedUser().getId())
          .stream()
          .map(share->mapper.map(share, SharingHistory.class)).collect(Collectors.toList());
          if(shares.isEmpty()){
            return ResponseEntity.ok("Your history is empty");
          }
          return ResponseEntity.ok(shares);     
          } catch (Exception e) {
            // TODO: handle exception 
            throw new Exception(e.getMessage());
          }
    }

    @Override
    public List<SharingHistory> findAllSharingHistoryByDocId(Long id_doss) throws Exception {
    try {
         
        DossierMedical dossierMedical=dossrepo.findById(id_doss).orElseThrow(()->new Exception("No Folder exist with this id"));
         List<SharingHistory> shares=sharinghistoryRepository.findAllSharingHistoryByDossierId(dossierMedical.getId_dossier())
          .stream()
          .map(share->mapper.map(share, SharingHistory.class)).collect(Collectors.toList());
          return shares;     
          } catch (Exception e) {
            // TODO: handle exception 
            throw new Exception(e.getMessage());
          }
    }

    @Override
    public SharingHistory findSharingHistoryById(Long id_share) throws Exception {
      try {
         
          SharingHistory share=sharinghistoryRepository.findById(id_share).orElseThrow(()->new Exception("No matchinf Found for this History sharing ID"));
          return share;     
          } catch (Exception e) {
            // TODO: handle exception 
            throw new Exception(e.getMessage());
          }
    }


    
 }
