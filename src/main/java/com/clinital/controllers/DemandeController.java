package com.clinital.controllers;

import java.util.List;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.dto.DemandeDTO;
import com.clinital.enums.DemandeStateEnum;
import com.clinital.models.Demande;
import com.clinital.payload.response.ApiResponse;
import com.clinital.services.DemandeServiceImpl;
import com.clinital.services.EmailSenderService;
import com.clinital.services.interfaces.DemandeService;
import com.clinital.util.ClinitalModelMapper;

@RestController
@RequestMapping("/api/demandes/")
public class DemandeController {
	
	@Autowired
	private DemandeServiceImpl demandeService;
	
	@Autowired
	EmailSenderService emailSenderService;
	
	@Autowired
	ClinitalModelMapper modelMapper;
	
	// A method that creates a demande.%OK%
	@PostMapping("create")
	public ResponseEntity<?> create(@RequestBody DemandeDTO demande){
		
		return ResponseEntity.ok(demandeService.create(demande));
	}
	
	// Updating a demande by id. %OK%
	@PutMapping("update/{id}")
	public ResponseEntity<DemandeDTO> update(@RequestBody DemandeDTO demande,@PathVariable Long id) throws Exception{
		
		DemandeDTO demande2 = demandeService.update(demande, id);
		
		return ResponseEntity.accepted().body(demande2);
	}
	
	// A method that returns all the demandes. %ok%
	@GetMapping("all")
	public ResponseEntity<List<DemandeDTO>> all(){
		
		return ResponseEntity.ok(demandeService.findAll());
	}
	
	// A method that returns a demande by id.%ok%
	@GetMapping("findbyid/{id}")
	public ResponseEntity<DemandeDTO> findById(@PathVariable Long id) throws Exception{
		
		return ResponseEntity.ok(demandeService.findById(id));
	}
	//
	@GetMapping("demandebystate/{state}")
	public ResponseEntity<List<Demande>> findBySateDemande(@Valid @PathVariable DemandeStateEnum state) throws Exception{
		
		return ResponseEntity.ok(demandeService.findByState(state)
		.stream().map(demnd -> modelMapper.map(demnd, Demande.class))
		.collect(Collectors.toList()));
	}


	// A method that deletes a demande by id. %ok%
	@DeleteMapping("delete/{id}")
	
	public ResponseEntity<?> deleteById(@PathVariable Long id) throws Exception{
		
		demandeService.deleteById(id);

		return  ResponseEntity.ok(new ApiResponse(true, " Demande has been deleted successfully "+HttpStatus.OK)  );
	}

	// Valider la demande : %OK%.
	@PostMapping("validerdmd/{id}")
	public ResponseEntity<Demande> ValidateDemande(@Valid @RequestBody DemandeDTO valide,@PathVariable Long id) throws Exception{
		
		Demande demande2 = demandeService.validate(valide.getValidation(),id);
		
		return ResponseEntity.accepted().body(demande2);
	}
	

}
