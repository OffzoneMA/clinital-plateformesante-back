package com.clinital.controllers;

import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.dto.VilleDTO;
import com.clinital.models.TypeConsultation;
import com.clinital.models.User;
import com.clinital.models.Ville;
import com.clinital.repository.UserRepository;
import com.clinital.repository.VilleRepository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.ActivityServices;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ville")
public class VilleController {

	@Autowired
	VilleRepository villerepo;

	@Autowired
	ClinitalModelMapper mapper;

	@Autowired
	ActivityServices activityServices;

	@Autowired
	GlobalVariables globalVariables;

	@Autowired
	private UserRepository UserRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@GetMapping("/allvilles")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	Iterable<VilleDTO> villes() {
	
		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","LOADING All cities ",globalVariables.getConnectedUser());
		LOGGER.info("loading all villes "+globalVariables.getConnectedUser().getId());
		}
		return villerepo.findAll().stream().map(ville -> mapper.map(ville, VilleDTO.class))
				.collect(Collectors.toList());
	}

}
