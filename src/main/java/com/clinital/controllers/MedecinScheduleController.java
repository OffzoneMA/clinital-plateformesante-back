package com.clinital.controllers;


import com.clinital.dto.MedecinDTO;
import com.clinital.dto.MedecinScheduleDto;
import com.clinital.models.Medecin;
import com.clinital.models.MedecinSchedule;
import com.clinital.payload.request.MedecinScheduleRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.payload.response.MedecinScheduleResponse;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.MedecinScheduleRepository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.MedecinScheduleServiceImpl;
import com.clinital.services.MedecinServiceImpl;
import com.clinital.services.interfaces.MedecinScheduleService;
import com.clinital.util.ApiError;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

@RestController
@RequestMapping("api/medecinSchedule/")
public class MedecinScheduleController {

    @Autowired
    private MedecinScheduleServiceImpl medecinScheduleService;

    @Autowired
    private MedecinScheduleRepository medshrep;

    @Autowired
    private ClinitalModelMapper modelMapper;

    @Autowired 
    private MedecinServiceImpl medserice;

    @Autowired 
    private MedecinScheduleRepository medsrepo;

    @Autowired
    GlobalVariables globalVariables;
// Creat a new Schedule : %OK%
    @PostMapping("create")
    // @PostAuthorize("hasAuthority('MEDECIN')")
    public ResponseEntity<?> create(@Valid @RequestBody MedecinScheduleRequest
        medecinScheduleRequest) throws Exception{

        // Getting the current user from the security context.
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        

        MedecinSchedule medecinSchedule = medecinScheduleService.create(medecinScheduleRequest,globalVariables.getConnectedUser().getId());


        return ResponseEntity.ok(modelMapper.map(medecinSchedule,MedecinSchedule.class));
    }


    @PostMapping("update/{id}")
    @ResponseBody
	@JsonSerialize(using = LocalDateTimeSerializer.class)
    //@PostAuthorize("hasAuthority('MEDECIN')")
    public ResponseEntity<?> update(@Valid @RequestBody MedecinScheduleRequest medecinScheduleRequest, @PathVariable Long id) throws Exception {

       try {
        Optional<MedecinSchedule> isSchedule=medsrepo.findById(id);
        if(isSchedule.isPresent()){
 return ResponseEntity.ok(medecinScheduleService.update(medecinScheduleRequest,id));
        }else{
            return ResponseEntity.accepted().body(new ApiError(HttpStatus.BAD_REQUEST, "update failed", null));
        }
        
       } catch (Exception e) {
        // TODO: handle exception
        throw new Exception(e);
       }
   

        
    }

    @DeleteMapping("delete/{id}")
    //@PostAuthorize("hasAuthority('MEDECIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) throws Exception{

        medecinScheduleService.deleteById(id);

        return ResponseEntity.ok(new ApiResponse(true, "Schedule has been deleted successfully"));

    }

@GetMapping("shedulebyMed")
@ResponseBody
public ResponseEntity<?> GetAllSchedulesByMedId() throws Exception{
    // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Medecin med = medserice.getMedecinByUserId(globalVariables.getConnectedUser().getId());
    try {
         
         return ResponseEntity.ok(modelMapper.map(medecinScheduleService.GetAllSchedulesByMedId(med.getId()),MedecinSchedule.class));
    } catch (Exception e) {
        throw new Exception(e);
    }

   
    
}
@GetMapping("shedulebyMedandIdconsult/{id}")
@ResponseBody
public ResponseEntity<?> GetAllSchedulesByMedIdandIdConsult(@Valid @PathVariable long id) throws Exception{
    // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Medecin med = medserice.getMedecinByUserId(globalVariables.getConnectedUser().getId());
    try {
        
            return ResponseEntity.ok(modelMapper.map(medecinScheduleService.GetAllSchedulesByMedIdandIdCOnsult(med.getId(),id), MedecinSchedule.class));
        
         
    } catch (Exception e) {
        throw new Exception(e);
    }

   
    
}
@GetMapping("shedulebyId/{id}")
@ResponseBody
public ResponseEntity<?> GetAllSchedulesByid(@Valid @PathVariable long id) throws Exception{
    // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Medecin med = medserice.getMedecinByUserId(globalVariables.getConnectedUser().getId());
    try {
        Optional<MedecinSchedule> Sched = medshrep.findById(id);
        if(Sched.isPresent()){
         return ResponseEntity.ok(modelMapper.map(medecinScheduleService.GetAllSchedulesByIdsched(id), MedecinSchedule.class));
        }
        else{
            return ResponseEntity.ok(new ApiResponse(false, "No matching Found"));
        } 
    } catch (Exception e) {
        throw new Exception(e);
    }

   
    
}
}
