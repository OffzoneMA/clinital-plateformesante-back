package com.clinital.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.dto.AntecedentsDTO;
import com.clinital.models.Antecedents;
import com.clinital.models.DossierMedical;
import com.clinital.models.Medecin;
import com.clinital.payload.request.AntecedentsRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.repository.AntecedentsRepository;
import com.clinital.repository.DossierMedicalRepository;
import com.clinital.services.interfaces.AntecedentsService;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;

@Transactional
@Service
public class AntecedentsServicesImpl implements AntecedentsService{

    @Autowired
    private DossierMedicalRepository dosserrepo;

    @Autowired
    private AntecedentsRepository antRepository;

    @Autowired
    private ClinitalModelMapper mapper;

    @Autowired
    private ActivityServices activityServices;

    @Autowired
    private GlobalVariables globalvariables;

    public final Logger LOGGER=LoggerFactory.getLogger(this.getClass());

    @Override
    public Antecedents create(AntecedentsRequest req,Medecin med) throws Exception {
        try {
            DossierMedical dossier=dosserrepo.getdossierByIdandMedId(med.getId(),req.getDossier()).orElseThrow(()->new Exception("You dont have access to this Medical Folder"));
            
            Antecedents antecedents=new Antecedents();
            antecedents.setDate(LocalDate.now());
            antecedents.setDescreption(req.getDescreption());
            antecedents.setType(req.getType());
            antecedents.setDossier(dossier);
            antRepository.save(antecedents);
            return antecedents;
            
                
            } catch (Exception e) {
            throw new Exception(e.getMessage());
            }
    }

    @Override
    public Antecedents update(AntecedentsRequest req,Medecin med) throws Exception {

         try {

            Antecedents antecedents=antRepository.findById(req.getId_anticedent()).orElseThrow(()->new Exception("No matching found for this Antecedents"));
            DossierMedical dossier=dosserrepo.getdossierByIdandMedId(med.getId(),req.getDossier()).orElseThrow(()->new Exception("No matching found for this dossier"));
            
            
            antecedents.setDate(req.getDate());
            antecedents.setDescreption(req.getDescreption());
            antecedents.setType(req.getType());
            antecedents.setDossier(dossier);
            antRepository.save(antecedents);
            return antecedents;
            
                
            } catch (Exception e) {
            throw new Exception(e.getMessage());
            }
    }

    @Override
    public List<AntecedentsDTO> findAll() throws Exception {
        try {
            List<AntecedentsDTO> allaAntecedents=antRepository.findAll().stream().map(consul->mapper.map(consul,AntecedentsDTO.class)).collect(Collectors.toList());
            return allaAntecedents;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public AntecedentsDTO findById(Long id) throws Exception {
        try {
            Antecedents antecedents=antRepository.findById(id).orElseThrow(()->new Exception("No matching found for this Antecedents"));

            return mapper.map(antecedents,AntecedentsDTO.class); 

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        
    }

   // Find By Doctor and access rights : 
  
    public List<Antecedents> findAllByMed(Medecin med) throws Exception {
        try {

            List<Antecedents> allAntecedents=antRepository.findAll()
            .stream()
            .filter(antecedents->med.getMeddossiers().contains(antecedents.getDossier()))
            .collect(Collectors.toList());
            activityServices.createActivity(new Date(), "Read", "Consulting All Anticedents", globalvariables.getConnectedUser());
            LOGGER.info("Consulting All Antecedents");
            return allAntecedents;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public ResponseEntity<?> findByIdandMed(Long id,Medecin med) throws Exception {
        try {
            Antecedents antecedents=antRepository.findById(id).orElseThrow(()->new Exception("No matching found for this Antecedents"));
            DossierMedical dossier=antecedents.getDossier();
            // check if the folder is already shared.
            Boolean isDossshared=med.getMeddossiers().stream().filter(doss->doss.getId_dossier()==dossier.getId_dossier()).findFirst().isPresent();

            if(isDossshared){
                activityServices.createActivity(new Date(), "Read", "Consulting this Anticedent ID "+antecedents.getId_anticedent(), globalvariables.getConnectedUser());
                LOGGER.info("Consulting Antecedent ID : "+antecedents.getId_anticedent());
            return ResponseEntity.ok(antecedents); 
            }else{
                activityServices.createActivity(new Date(), "Read", "you Cant get access to this resource Anticedent ID "+antecedents.getId_anticedent(), globalvariables.getConnectedUser());
                LOGGER.info("you Cant get access to this resource Antecedent ID : "+antecedents.getId_anticedent());
                return ResponseEntity.ok(new ApiResponse(false, "you Cant access to this resource"));
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        
    }


    @Override
    public Boolean deleteAntecedent(Antecedents Antecedents) throws Exception {
        
        try {
            
            Boolean IsDeleted= AntecedentsRepository.deleteAntecedent(Antecedents.getId_anticedent())?true:false;
            return IsDeleted;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
}
