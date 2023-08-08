package com.clinital.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.clinital.dto.UserDTO;
import com.clinital.enums.CiviliteEnum;
import com.clinital.enums.PatientTypeEnum;
import com.clinital.models.*;
import com.clinital.repository.VilleRepository;
import com.clinital.services.PatientService;
import com.clinital.services.UserServiceImpl;
import com.clinital.services.interfaces.UserService;
import com.clinital.util.GlobalVariables;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.clinital.enums.ERole;
import com.clinital.enums.ProviderEnum;
import com.clinital.exception.BadRequestException;
import com.clinital.payload.request.LoginRequest;
import com.clinital.payload.request.SignupRequest;
import com.clinital.payload.request.VerifyEmailRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.payload.response.JwtResponse;
import com.clinital.payload.response.MessageResponse;
import com.clinital.payload.response.FacebookResponse;
import com.clinital.payload.response.GoogleResponse;
import com.clinital.repository.RoleRepository;
import com.clinital.repository.UserRepository;
import com.clinital.security.ConfirmationToken;
import com.clinital.security.jwt.JwtUtils;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.security.services.UserDetailsServiceImpl;
import com.clinital.services.ActivityServices;
import com.clinital.services.AutService;
import com.clinital.services.EmailSenderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AutService authService;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailsServiceImpl userDetService;

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	PatientService patientService;

	@Autowired
	ActivityServices activityServices;

	@Autowired
	UserServiceImpl userserices;

	@Autowired
    private GlobalVariables globalVariables;


	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

// A POST request to the /signin endpoint. %ok%
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

	

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		// List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
		// 		.collect(Collectors.toList());
		globalVariables.setConnectedUser();

		User user = userRepository.findById(userDetails.getId()).get();
		System.out.println(".()"+user.getEmail());
		if(userDetails.isEnabled()==false){
			return ResponseEntity.ok("Your Account is Blocked please try to Contact Clinital Admin");
		}
		
		if (user.getEmailVerified() == true) {

			userDetService.updateLastLoginDate(userDetails.getId());

			//save activity of user login
			activityServices.createActivity(new Date(), "Login", "Authentication reussi", user);
			LOGGER.info("Authentication reussi");
			 return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(),
			 		userDetails.getTelephone(), userDetails.getRole()));
			// return ResponseEntity.ok(new JwtResponse(userDetails.getEmail(),userDetails.getPassword(),
			// 		userDetails.getTelephone()));
		} else {
			return ResponseEntity.ok(new ApiResponse(false, "Email Not Verified"));
		}
	}

// Creating a new user. %OK%
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws Exception {

		userserices.RegistreNewUser(signUpRequest);
		
		return ResponseEntity.ok(new ApiResponse(true, "User registered successfully et un lien de verification a ete enovyé par mail "));
		
		
	}

	// A method that is called when the user clicks on the link in the email.%OK%
	@GetMapping("confirmaccount")
	public ResponseEntity<?> getMethodName(@RequestParam String token) {

		ConfirmationToken confirmationToken = authService.findByConfirmationToken(token);

		if (confirmationToken == null) {
			throw new BadRequestException("Invalid token");
		}

		User user = confirmationToken.getUser();
		Calendar calendar = Calendar.getInstance();

		if ((confirmationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
			return ResponseEntity.badRequest()
					.body("Lien expiré, generez un nouveau lien http://localhost:4200/signin");
		}

		user.setEmailVerified(true);
		user.setEnabled(true);
		authService.save(user);
		LOGGER.info("Account verified successfully :"+user.getEmail());
		return ResponseEntity.ok("this Account verified successfully!");
		
	}

	// Checking the validity of a token.
	@GetMapping("/checkToken/{token}")
	public Boolean CheckTokenValidity(@Valid @PathVariable String token){
	
		return jwtUtils.validateJwtToken(token);
	}



	

	/*@PostMapping("/sendmail")
	public ResponseEntity<?> sendVerificationMail(@Valid @RequestBody VerifyEmailRequest emailRequest) {
		if (authService.existsByEmail(emailRequest.getEmail())) {
			if (userDetService.isAccountVerified(emailRequest.getEmail())) {
				throw new BadRequestException("Email est déja verifié ");
			} else {
				User user = authService.findByEmail(emailRequest.getEmail());
				ConfirmationToken token = authService.createToken(user);
				emailSenderService.sendMail(user.getEmail(), token.getConfirmationToken());
				return ResponseEntity.ok(new ApiResponse(true, "un lien de verification a ete enovyé par mail "));
			}
		} else {
			throw new BadRequestException("Email non associé ");
		}*/


	@PostMapping("/fbLogin")
	public ResponseEntity<?> getFacebookProfileInfo(final String accessToken) throws BadRequestException {
		log.debug("Calling Facebook API to validate and get profile info");
		RestTemplate restTemplate = new RestTemplate();
		// field names which will be retrieved from facebook
		final String fields = "id,email,first_name,last_name";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://graph.facebook.com/me")
					.queryParam("access_token", accessToken).queryParam("fields", fields);

			log.debug("Facebook profile uri {}", uriBuilder.toUriString());
			FacebookResponse socialResponse = restTemplate.getForObject(uriBuilder.toUriString(),
					FacebookResponse.class);

			log.info("Facebook user authenticated and profile fetched successfully, details [{}]",
					socialResponse.toString());

			userDetService.processOAuthPostLogin(socialResponse.getEmail(), ProviderEnum.FACEBOOK);

			UserDetails userDetails = userDetService.loadUserByUsername(socialResponse.getEmail());

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null);
			authentication.setDetails(new WebAuthenticationDetailsSource());

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
			// List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
			// 		.collect(Collectors.toList());
			
			User user = userRepository.findById(userDetailsImpl.getId()).get();
			activityServices.createActivity(new Date(), "Login", "authentication successfully with Facebook ", user);
			LOGGER.info("Account Login with facebook :"+user.getEmail());
			return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getEmail(),
			 		userDetailsImpl.getTelephone(), userDetailsImpl.getRole()));
		} catch (HttpClientErrorException e) {
			log.error("Not able to authenticate from Facebook");
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid access token"));

		} catch (Exception exp) {
			log.error("User is not authorized to login into system", exp);
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid user"));

		}
	}

	@GetMapping("/oauth2/authorize")
	public Object CurrentUser(OAuth2AuthenticationToken auth2AuthenticationToken){
		return auth2AuthenticationToken.getPrincipal().getAttributes();

	}

	@PostMapping("/googleLogin")
	public ResponseEntity<?> getGoogleTokenInfo(String accessToken) throws BadRequestException {
		log.debug("Calling Google API to get token info");
		RestTemplate restTemplate = new RestTemplate();
		GoogleResponse googleResponse = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromUriString("https://www.googleapis.com/oauth2/v3/userinfo")
					.queryParam("access_token", accessToken);
			log.debug("google login uri {}", uriBuilder.toUriString());
			googleResponse = restTemplate.getForObject(uriBuilder.toUriString(), GoogleResponse.class);
			log.info("Gmail user authenticated successfully, details [{}]", googleResponse.toString());

			userDetService.processOAuthPostLogin(googleResponse.getEmail(), ProviderEnum.google);

			UserDetails userDetails = userDetService.loadUserByUsername(googleResponse.getEmail());

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource());

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
			// List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
			// 		.collect(Collectors.toList());
			
			User user = userRepository.findById(userDetailsImpl.getId()).get();
			activityServices.createActivity(new Date(), "Login", "authentication successfully with Google Login ", user);
			LOGGER.info("Account login successfully with Google :"+user.getEmail());
			return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getEmail(),
			 		userDetailsImpl.getTelephone(), userDetailsImpl.getRole()));
			
		} catch (HttpClientErrorException e) {
			log.error("Not able to authenticate from Google");
			try {
				JsonNode error = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
				log.error(error.toString());
				return ResponseEntity.badRequest().body(new MessageResponse("Invalid access token"));
			} catch (IOException mappingExp) {
				return ResponseEntity.badRequest().body(new MessageResponse("Invalid user"));
			}
		} catch (Exception exp) {
			log.error("User is not authorized to login into system", exp);
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid user"));
		}
	}

}
