package com.clinital.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.models.Consultation;
import com.clinital.models.DossierMedical;
import com.clinital.models.Medecin;
import com.clinital.models.Patient;
import com.clinital.models.Rendezvous;
import com.clinital.dto.ConsultationDTO;
import com.clinital.exception.BadRequestException;
import com.clinital.payload.request.ConsultationRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.repository.ConsultationRepository;
import com.clinital.repository.DossierMedicalRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.PatientRepository;
import com.clinital.repository.RdvRepository;
import com.clinital.services.interfaces.ConsultaionService;
import com.clinital.util.ClinitalModelMapper;

@Transactional
@Service
public class ConsultationServices implements ConsultaionService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedecinRepository medecinRepository;
    @Autowired
    private RdvRepository rdvRepository;
    @Autowired
    private ConsultationRepository consulrepo;
    @Autowired
    private ClinitalModelMapper mapper;
    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;

    @Override
    public Consultation create(ConsultationRequest creq,Medecin med) throws Exception {
        try {
        
        Patient pat=patientRepository.findById(creq.getPatient()).orElseThrow(()->new Exception("No matching found for this patient"));
        DossierMedical dossierMedical= dossierMedicalRepository.getdossierByIdandMedId(med.getId(),pat.getDossierMedical().getId_dossier()).orElseThrow(()->new BadRequestException("You dont have access right for this folder"));
        Rendezvous rdv=rdvRepository.findById(creq.getRendezvous()).orElseThrow(()->new Exception("No matching found for this Rendezvous"));
        Consultation consul=new Consultation();
        consul.setDate(LocalDate.now());
        consul.setDetails(creq.getDetails());
        consul.setMedecin(med);
        consul.setPatient(pat);
        consul.setRendezvous(rdv);
        consul.setDossier(dossierMedical);
        consulrepo.save(consul);
        return consul;
        
            
        } catch (Exception e) {
        throw new Exception(e.getMessage());
        }

    }

    @Override
    public ConsultationDTO update(ConsultationRequest creq,Medecin med) throws Exception {
        try {
            
            Patient pat=patientRepository.findById(creq.getPatient()).orElseThrow(()->new Exception("No matching found for this patient"));
            DossierMedical dossierMedical= dossierMedicalRepository.getdossierByIdandMedId(med.getId(),pat.getDossierMedical().getId_dossier()).orElseThrow(()->new BadRequestException("You dont have access right for this folder"));
            Consultation consul=consulrepo.findById(creq.getId_consultation()).orElseThrow(()->new Exception("No matching found for this Consultation"));
            Rendezvous rdv=rdvRepository.findById(creq.getRendezvous()).orElseThrow(()->new Exception("No matching found for this Rendezvous"));
            System.out.println("details : "+creq.getDetails());
            consul.setDate(creq.getDate());
            consul.setDetails(creq.getDetails());
            consul.setMedecin(med);
            consul.setPatient(pat);
            consul.setRendezvous(rdv);
            consul.setDossier(dossierMedical);
            consulrepo.save(consul);
            return mapper.map(consul,ConsultationDTO.class);
               
            } catch (Exception e) {
            throw new Exception(e.getMessage());
            }
    
    }

    // admin access :
    @Override
    public List<ConsultationDTO> findAll() throws Exception {
        try {
            List<ConsultationDTO> allconsultation=consulrepo.findAll().stream().map(consul->mapper.map(consul,ConsultationDTO.class)).collect(Collectors.toList());
            return allconsultation;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }

    @Override
    public ConsultationDTO findById(Long id) throws Exception {
        try {
            Consultation consultation=consulrepo.findonebyAdmin(id).orElseThrow(()->new Exception("No matching found for this Consultation"));
            return mapper.map(consultation,ConsultationDTO.class);  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }
//---------------------------------------
    //Patient
    public ConsultationDTO findByIdPatient(Long idcons,Long idpat) throws Exception {
        try {
            Patient pat=patientRepository.findById(idpat).orElseThrow(()->new Exception("No matching found for this patient"));
            Consultation consultation=consulrepo.findByIdpatientandId(idcons,pat.getId()).orElseThrow(()->new Exception("No matching found for this Consultation"));
            return mapper.map(consultation,ConsultationDTO.class);  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }

    public List<ConsultationDTO> findALLByIdPatient(Long idpat) throws Exception {
        try {
            Patient pat=patientRepository.findById(idpat).orElseThrow(()->new Exception("No matching found for this patient"));
            List<ConsultationDTO> allconsultation=consulrepo.findAllByIdPatient(pat.getId()).stream().map(consul->mapper.map(consul,ConsultationDTO.class)).collect(Collectors.toList());
            return allconsultation;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }
//------------------------------------
    //Medecin
    public ConsultationDTO findByIdMedecin(Long idcons,Long idmed) throws Exception {
        try {
            Consultation consultation=consulrepo.findByIdmedecinandId(idcons,idmed).orElseThrow(()->new Exception("No matching found for this Consultation"));
            return mapper.map(consultation,ConsultationDTO.class);  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }

    public List<ConsultationDTO> findALLByIdMedecin(Long idmed) throws Exception {
        try {
            List<ConsultationDTO> allconsultation=consulrepo.findAllByIdMedecin(idmed).stream().map(consul->mapper.map(consul,ConsultationDTO.class)).collect(Collectors.toList());
            return allconsultation;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }



    @Override
    public Boolean deleteById(Consultation consultation) throws Exception {
        try {
            
            Boolean IsDeleted= ConsultationRepository.deleteConsultation(consultation.getId_consultation())?true:false;
            return IsDeleted;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Consultation> findByIdName(String name) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ResponseEntity<?> findByIdMedandDossierId(Medecin med,Long iddoss,Long idordo) throws Exception {
        try {
            
		DossierMedical dossier = dossierMedicalRepository.findById(iddoss).orElseThrow(()->new Exception("NO such Folder exist"));


        Consultation consultation=consulrepo.findIdandIdDossier(idordo, iddoss).orElseThrow(()->new Exception("No Matchng found for this Id ordonnace in this patient folder"));
		// check if the folder is already shared.
		Boolean isDossshared=med.getMeddossiers().stream().filter(doss->doss.getId_dossier()==dossier.getId_dossier()).findFirst().isPresent();

        if(isDossshared){
           return ResponseEntity.ok(consultation); 
        }else{

            return ResponseEntity.ok(new ApiResponse(false, "you Cant access to this resource"));
		}

           
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }

}
