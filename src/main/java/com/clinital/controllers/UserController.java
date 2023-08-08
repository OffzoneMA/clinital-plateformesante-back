package com.clinital.controllers;

import java.util.Date;

import javax.validation.Valid;

import com.clinital.dto.UserDTO;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.models.Medecin;
import com.clinital.models.User;
import com.clinital.payload.request.LoginRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.payload.response.MessageResponse;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.UserRepository;
import com.clinital.security.CurrentUser;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.ActivityServices;
import com.clinital.services.UserServiceImpl;
import com.clinital.services.interfaces.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserServiceImpl userservceimpl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MedecinRepository medRepository;

	@Autowired
	private ClinitalModelMapper modelMapper;

	@Autowired
	ActivityServices activityServices;

	@Autowired
	GlobalVariables globalVariables;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@PutMapping("{id}")
	@PreAuthorize("hasRole('ROLE_PATIENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MEDECIN') or hasRole('ROLE_SECRETAIRE')")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody User newUser,
			@PathVariable(value = "id") String email, @CurrentUser UserDetailsImpl currentUser) {
		User updatedUSer = userService.updateUser(newUser, email, currentUser);

		UserDTO mapped = modelMapper.map(updatedUSer, UserDTO.class);
		activityServices.createActivity(new Date(), "Update", "Update User Account Inforamtion ",
				globalVariables.getConnectedUser());
		LOGGER.info("Update User Account Information, UserID : " + globalVariables.getConnectedUser().getId());
		return ResponseEntity.accepted().body(mapped);
	}

	// A method that resets the password of a user. %ok%
	@PostMapping("/respw")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody LoginRequest loginRequest) {
		// UserDetailsImpl userDetails = (UserDetailsImpl)
		// SecurityContextHolder.getContext().getAuthentication()
		// .getPrincipal();
		User user = userRepository.getById(globalVariables.getConnectedUser().getId());

		if (userservceimpl.changePassword(user, loginRequest.getPassword())) {

			activityServices.createActivity(new Date(), "Update", "Password changed successfully",
					globalVariables.getConnectedUser());
			LOGGER.info("Password changed successfully, UserID : " + globalVariables.getConnectedUser().getId());
			return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Unable to change password. Try again!"));
		}

	}

	// A method that blocks a user.
	@PostMapping("/block/user/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> BlockUser(@Valid @PathVariable long id) throws Exception {
		User user = userRepository.findById(id).orElseThrow(() -> new Exception("No Matching Found"));
		if (userservceimpl.BlockUser(user.getId())) {

			String state = (user.isEnabled()) ? "Deblocked" : "Blocked";
			activityServices.createActivity(new Date(), "Update", "User has been " + state,
					globalVariables.getConnectedUser());
			LOGGER.info("User has been " + state + " Seccessefully, UserID : "
					+ globalVariables.getConnectedUser().getId());

			return ResponseEntity.ok(new ApiResponse(true, "User is " + state + " successfully"));
		} else {
			activityServices.createActivity(new Date(), "Error", "Unable to Block User Try again!",
					globalVariables.getConnectedUser());
			LOGGER.error("Unable to Block User ID : " + user.getId() + " Try again!, UserID : "
					+ globalVariables.getConnectedUser().getId());
			return ResponseEntity.badRequest().body(new MessageResponse("Unable to Block User Try again!"));
		}

	}

	// A method that Limit med visibility.
	@PostMapping("/block/med/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> VisibilityMedecin(@Valid @PathVariable long id) throws Exception {
Medecin med = medRepository.findById(id).orElseThrow(()->new Exception("No Matching Found"));
		if (userservceimpl.ShowMedecin(id)) {
			
			String state = (med.getIsActive()) ? "Visible" : "Invisivle";
			activityServices.createActivity(new Date(), "Update", "Medecin ID : "+med.getId()+"has been " + state + " Seccessefully", globalVariables.getConnectedUser());
			LOGGER.info("Medecin ID : "+med.getId()+"has been " + state + " Seccessefully, UserID : " + id);
			return ResponseEntity.ok(new ApiResponse(true, "Medecin is " + state + " successfully"));
		} else {
			activityServices.createActivity(new Date(), "Error", "Unable to Change Medecin Visibility User Try again!",
					globalVariables.getConnectedUser());
			LOGGER.error("Unable to Change Medecin Visibility User Try again!, UserID : "
					+ globalVariables.getConnectedUser().getId());
			return ResponseEntity.badRequest().body(new MessageResponse("Unable to Change Medecin Visibility User Try again!"));
		}

	}

}
