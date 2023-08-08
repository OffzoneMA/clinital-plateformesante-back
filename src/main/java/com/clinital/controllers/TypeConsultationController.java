package com.clinital.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.dto.MedecinDTO;
import com.clinital.dto.TypeConsultationDTO;
import com.clinital.exception.BadRequestException;
import com.clinital.models.Medecin;
import com.clinital.models.TypeConsultation;
import com.clinital.payload.request.TypeConsultationRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.payload.response.TypeConsultationResponse;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.TypeConsultationRepository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.ActivityServices;
import com.clinital.services.MedecinServiceImpl;
import com.clinital.services.TypeConsultationServicesImpli;
import com.clinital.util.ApiError;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/typeConsultation")
public class TypeConsultationController {

	@Autowired
	TypeConsultationRepository typeConsultationRepository;

	@Autowired
	MedecinServiceImpl medService;

	@Autowired
	ClinitalModelMapper mapper;

	@Autowired
	MedecinRepository Medrepo;

	@Autowired
    GlobalVariables globalVariables;

	@Autowired
	TypeConsultationServicesImpli typecosnultationServices;

	
	@Autowired
	private ActivityServices activityServices;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	

	// A method that returns a list of TypeConsultationResponse objects.
	@GetMapping("/getAllByMedecinId/{medecinId}")
	@ResponseBody
	public List<TypeConsultationResponse> findAllTypeConsultationByMedecinId(
			@PathVariable Long medecinId) throws Exception {

		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
				
		Boolean medecin = Medrepo.existsById(medecinId);

		if (medecin)
		{activityServices.createActivity(new Date(), "Read", "Consult All Consultation By Medecin ID : " + medecinId,
					globalVariables.getConnectedUser());
			LOGGER.info("Consult All Consultation By Medecin ID : " + medecinId + ", UserID : " + globalVariables.getConnectedUser().getId());
			return typecosnultationServices.findAllTypeConsultationByMedecinId(medecinId).stream()
					.map(consult -> mapper.map(consult, TypeConsultationResponse.class)).collect(Collectors.toList());}
		else
			return null;
	}

	// A method that returns a TypeConsultationResponse object by Medecin ID.
	@GetMapping("/getTypeConsultationById/{id}")
	@ResponseBody
	public TypeConsultationResponse findTypeConsultationById(@PathVariable Long id) throws Exception {

		TypeConsultationResponse typeConsultationResponse = null;
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin medecin = medService.getMedecinByUserId(globalVariables.getConnectedUser().getId());

		typeConsultationResponse = mapper.map(
				typeConsultationRepository.findById(id)
						.orElseThrow(() -> new BadRequestException("Type Consultation not found for this id :: " + id)),
				TypeConsultationResponse.class);

		if (medecin.getId() == typeConsultationResponse.getMedecinId())
		{activityServices.createActivity(new Date(), "Read", "Consult Consultation Type By Medecin ID : " + medecin.getId(),
		globalVariables.getConnectedUser());
		LOGGER.info("Consult Consultation Type By Medecin ID : " + medecin.getId() + ", UserID : " + globalVariables.getConnectedUser().getId());
			return typeConsultationResponse;}
		else
			return null;
	}

	// Adding a new TypeConsultation object.
	@PostMapping("/addTypeConsultation")
	public ResponseEntity<?> addTypeConsultation(@Valid @RequestBody TypeConsultationRequest typeConsultation) throws Exception {

		TypeConsultation typeConsult =  typecosnultationServices.addTypeConsultation(typeConsultation);
		activityServices.createActivity(new Date(), "Add", "Add new Type Consultation " ,
					globalVariables.getConnectedUser());
			LOGGER.info("Add new Type COnsultation, UserID : " + globalVariables.getConnectedUser().getId());
		return ResponseEntity.ok(mapper.map(typeConsult,TypeConsultation.class));

		
	
	}
// Delete a Type consulation by Id : %OK%
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteTypeConsultation(@PathVariable Long id) throws Exception {
					UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
					Medecin Med = Medrepo.getMedecinByUserId(globalVariables.getConnectedUser().getId());
					Optional<TypeConsultation> typ=typeConsultationRepository.findById(id);
					if(typ.isPresent()){

						typecosnultationServices.deleteTypeConsultation(id,Med.getId());
						return ResponseEntity.ok(new ApiResponse(true, "TtypeConsult has been deleted Seccussefully"));
					}
					else return ResponseEntity.ok(new ApiResponse(false, "No match for this ID"));
			
	}

// Updating a TypeConsultation object by ID.
	@PutMapping("/updateTypeConsultation/{id}")
	public ResponseEntity<?> updateTypeConsultation(
			@Valid @RequestBody TypeConsultationRequest typeConsultation, @PathVariable Long id) throws Exception {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
				Medecin Med = Medrepo.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		if (Med.getId() == typeConsultation.getMedecinId()) {

			  TypeConsultationDTO typeConsultationUpdated = typecosnultationServices.updateTypeConsultation(typeConsultation, id);

			return ResponseEntity.ok(mapper.map(typeConsultationUpdated, TypeConsultation.class));
		} else
			throw new BadRequestException("Not allowed");
	}

}
