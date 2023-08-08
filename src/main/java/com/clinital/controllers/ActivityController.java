package com.clinital.controllers;

import java.util.stream.Collectors;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.dto.ActivityDTO;
import com.clinital.payload.response.activityLogResponse;
import com.clinital.repository.ActivityRespository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.interfaces.historyservices;
import com.clinital.util.ClinitalModelMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users/activity")
// @PreAuthorize("hasAnyRole('ROLE_PATIENT','ROLE_ADMIN','ROLE_SECRETAIRE','ROLE_MEDECIN')")
public class ActivityController {
    @Autowired 
    ActivityRespository  activityRepo;

    @Autowired
    historyservices historyservices;

    @Autowired
	ClinitalModelMapper mapper;

// Get All Users activities : 
    @GetMapping("/allactivity")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    Iterable<ActivityDTO> activity() {
        return activityRepo.findAll().stream().map(activity -> mapper.map(activity, ActivityDTO.class))
                .collect(Collectors.toList());
    }

// Get Activity By User Id
    @GetMapping("/getActivityByIdUser/{id}")
	@ResponseBody
	Iterable<ActivityDTO> findById(@PathVariable(value = "id") Long userID) throws Exception {
		//return mapper.map(historyservices.getactivityByIdUser(userID), activityLogResponse.class);
        return activityRepo.findActivityByIdUsers(userID).stream().map(activity -> mapper.map(activity, ActivityDTO.class))
                .collect(Collectors.toList());

	}
// Get activity of the current User
    @GetMapping("/myactivity")
	@ResponseBody
	Iterable<ActivityDTO> findMyactivity(@PathVariable(value = "id") Long userID) throws Exception {

		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return activityRepo.findActivityByIdUsers(user.getId()).stream().map(activity -> mapper.map(activity, ActivityDTO.class))
                .collect(Collectors.toList());

	}

    
}
