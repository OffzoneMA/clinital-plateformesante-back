package com.clinital.controllers;

import java.util.Date;

import javax.faces.annotation.RequestMap;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.dto.AntecedentsDTO;
import com.clinital.models.Antecedents;
import com.clinital.models.DossierMedical;
import com.clinital.models.Medecin;
import com.clinital.models.User;
import com.clinital.payload.request.AntecedentsRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.repository.AntecedentsRepository;
import com.clinital.repository.UserRepository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.ActivityServices;
import com.clinital.services.AntecedentsServicesImpl;
import com.clinital.services.MedecinServiceImpl;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/med/antecedent")

public class AntecedentsController {

    @Autowired
    private AntecedentsServicesImpl antecedentsServicesImpl;

    @Autowired
    private MedecinServiceImpl medecinServiceImpl;

    @Autowired
    private ClinitalModelMapper mapper;

    @Autowired
    private AntecedentsRepository antecedentsRepository;

    @Autowired ActivityServices activityServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GlobalVariables globalVariables;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/addnewantecedent")
    public ResponseEntity<?> AddNew(@Valid @RequestBody AntecedentsRequest request){

        try {
            // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
			// 	.getPrincipal();
	        Medecin med = medecinServiceImpl.getMedecinByUserId(globalVariables.getConnectedUser().getId());
            Antecedents eAntecedent=antecedentsServicesImpl.create(request, med);
            activityServices.createActivity(new Date(), "Add", "Anticedent ID "+eAntecedent.getId_anticedent()+" has been Add Seccuessfully", globalVariables.getConnectedUser());
            LOGGER.info("Antecedent has been Add ID : "+eAntecedent.getId_anticedent());
           return ResponseEntity.ok(mapper.map(eAntecedent, AntecedentsDTO.class));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
        
    }

    @PostMapping("/updateantecedent")
    public ResponseEntity<?> UpdateAntecedent(@Valid @RequestBody AntecedentsRequest request){

        try {
            // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
			// 	.getPrincipal();
	        Medecin med = medecinServiceImpl.getMedecinByUserId(globalVariables.getConnectedUser().getId());
            Antecedents eAntecedent=antecedentsServicesImpl.update(request, med);
            activityServices.createActivity(new Date(), "Update", "Anticedent ID "+eAntecedent.getId_anticedent()+" has been Updated Seccuessfully", globalVariables.getConnectedUser());
            LOGGER.info("Antecedent has been Updated  ID : "+eAntecedent.getId_anticedent());
           return ResponseEntity.ok(mapper.map(eAntecedent, AntecedentsDTO.class));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
        
    }

    @DeleteMapping("/deleteantecedent/{id}")
    public ResponseEntity<?> DeleteAntecedent(@Valid @PathVariable("id") Long idant){

        try {
            // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
			// 	.getPrincipal();
	        Medecin med = medecinServiceImpl.getMedecinByUserId(globalVariables.getConnectedUser().getId());
            Antecedents antecedents=antecedentsRepository.findById(idant).orElseThrow(()->new Exception("No matching found for this Antecedents"));
            DossierMedical dossier=antecedents.getDossier();
            // check if the folder is already shared.
            Boolean isDossshared=med.getMeddossiers().stream().filter(doss->doss.getId_dossier()==dossier.getId_dossier()).findFirst().isPresent();

            if(isDossshared){

               // Calling the deleteAntecedent method in the AntecedentsServicesImpl class.
                if(antecedentsServicesImpl.deleteAntecedent(antecedents)){
                    activityServices.createActivity(new Date(), "Delete", "Anticedent ID "+antecedents.getId_anticedent()+" has been Delete Seccuessfully", globalVariables.getConnectedUser());
                    LOGGER.info("Antecedent has been Deleted : "+antecedents.getId_anticedent());
                    return ResponseEntity.ok("Antecedent has been Deleted");
                }else{
                    activityServices.createActivity(new Date(), "Delete", "Anticedent ID "+antecedents.getId_anticedent()+" Something wrong is happen while Deleting this object.", globalVariables.getConnectedUser());
                    LOGGER.info("Antecedent ID : "+antecedents.getId_anticedent()+" Something wrong is happen while Deleting this object.");
                    return ResponseEntity.ok("Something wrong is happen");
                }
                
            }else{
                activityServices.createActivity(new Date(), "Delete", "Anticedent ID "+antecedents.getId_anticedent()+" you Cant access to this resource", globalVariables.getConnectedUser());
                LOGGER.info("You Cant access to this resource, Antecedent ID: "+antecedents.getId_anticedent());
                return ResponseEntity.ok(new ApiResponse(false, "you Cant access to this resource"));
            }
           
     
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
        
    }

    @GetMapping(path = "/findantecedents/{id}")
    public ResponseEntity<?> findAntecedentByIdMedecin(@Valid @PathVariable Long id){ 
        try {
            // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
			// 	.getPrincipal();
            //User user=userRepository.getById(globalVariables.getConnectedUser().getId());
	        Medecin med = medecinServiceImpl.getMedecinByUserId(globalVariables.getConnectedUser().getId());
        
            return  ResponseEntity.ok(antecedentsServicesImpl.findByIdandMed(id, med));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }   

    @GetMapping(path = "/findallantecedents")
    public ResponseEntity<?> findallAntecedentByMedecin(){ 
        try {
            // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
			// 	.getPrincipal();
	        Medecin med = medecinServiceImpl.getMedecinByUserId(globalVariables.getConnectedUser().getId());
            return  ResponseEntity.ok(antecedentsServicesImpl.findAllByMed(med));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }
    
}
