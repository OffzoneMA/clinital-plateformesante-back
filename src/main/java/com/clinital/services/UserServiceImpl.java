package com.clinital.services;

import com.clinital.dto.UserDTO;
import com.clinital.enums.ERole;
import com.clinital.enums.PatientTypeEnum;
import com.clinital.enums.ProviderEnum;
import com.clinital.models.Demande;
import com.clinital.models.DossierMedical;
import com.clinital.models.Medecin;
import com.clinital.models.Patient;
import com.clinital.models.Secretaire;
import com.clinital.models.Specialite;
import com.clinital.models.Ville;
import com.clinital.payload.request.SignupRequest;
import com.clinital.payload.response.MessageResponse;
import com.clinital.repository.DossierMedicalRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.PatientRepository;
import com.clinital.repository.SecretaireRepository;
import com.clinital.repository.SpecialiteRepository;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.exception.BadRequestException;
import com.clinital.models.User;
import com.clinital.repository.UserRepository;
import com.clinital.security.ConfirmationToken;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.interfaces.UserService;
import com.clinital.util.ApiError;
import com.clinital.util.ClinitalModelMapper;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DossierMedicalRepository dossierMedicalRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private MedecinRepository medRepository;

	@PersistenceContext
	private EntityManager entityManger;

	@Autowired
	private ClinitalModelMapper mapper;

	@Autowired
	private MedecinRepository medrepo;

	@Autowired
	private AutService authService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	SecretaireServiceImpl secriservices;

	@Autowired
	SecretaireRepository secretrepos;

	@Autowired
	ActivityServices activityServices;

	@Autowired
	SpecialiteRepository specialrepo;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public User updateUser(User newUser, String email, UserDetailsImpl currentUser) {

		User user = userRepository.findByEmail(email);
		if (user != null) {
			user.setEmail(newUser.getEmail());
			user.setTelephone(newUser.getTelephone());
			user.setPassword(passwordEncoder.encode(newUser.getPassword()));

			return userRepository.save(user);

		} else
			throw new BadRequestException("email not found");

	}

	// we have use here EntityManager to prevent double Pk with new entity
	@Override
	public User save(User user, Object obj) {

		try {

			switch (user.getRole()) {
				case ROLE_PATIENT:
					// save patient with dossier medical automatically
					Ville ville = new Ville();
					ville.setId_ville(1L);
					// generer un dossier medical par defaut
					DossierMedical dossierMedical = new DossierMedical();
					dossierMedical.setAlchole(false);
					dossierMedical.setFumeur(false);
					dossierMedical.setAccesscode(null);
					dossierMedical.setDossierType(null);
					dossierMedical.setNumDossier(null);
					dossierMedical.setTraitement(true);
					dossierMedicalRepository.save(dossierMedical);
					// genrer un patient par defaut
					Patient patient = new Patient();
					patient.setNom_pat(null);
					patient.setPrenom_pat(null);
					patient.setDateNaissance(null);
					patient.setAdresse_pat(null);
					patient.setCodePost_pat(null);
					patient.setMatricule_pat(null);
					patient.setCivilite_pat(null);
					patient.setPatientEmail(user.getEmail());
					patient.setVille(ville);
					patient.setDossierMedical(dossierMedical);
					patient.setDossierMedical(new DossierMedical());
					patient.setPatient_type(PatientTypeEnum.MOI);
					patient.setUser(user);
					patientRepository.save(patient);
					LOGGER.info("New Patient has been add ");
					break;
				case ROLE_MEDECIN:
					Demande demande = mapper.map(obj, Demande.class);
					Specialite specialite=specialrepo.getSpecialiteByName(demande.getSpecialite());
					Medecin medecin = new Medecin();
					medecin.setNom_med(demande.getNom_med());
					medecin.setPrenom_med(demande.getPrenom_med());
					medecin.setMatricule_med(demande.getMatricule());
					medecin.setInpe(demande.getInpe());
					medecin.setPhoto_med(null);
					medecin.setPhoto_couverture_med(null);
					medecin.setDescription_med(null);
					medecin.setContact_urgence_med(null);
					medecin.setCivilite_med(demande.getCivilite_med());
					medecin.setUser(user);
					medecin.setIsActive(false);
					medecin.setDiplome_med(null);
					medecin.setVille(null);
					medecin.setSpecialite(specialite);
					medecin.setStepsValidation(1L);
					medrepo.save(medecin);
					LOGGER.info("New Medecin has been add ");
					break;
				case ROLE_SECRETAIRE:
					Secretaire secrit = new Secretaire();
					secrit.setNom(null);
					secrit.setPrenom(null);
					secrit.setAdresse(null);
					secrit.setDateNaissance(null);
					secrit.setUser(user);
					secrit.setCabinet(null);
					secretrepos.save(secrit);
					LOGGER.info("New Secetaire has been add ");

				default:
					break;
			}

			ConfirmationToken token = authService.createToken(user);
			emailSenderService.sendMail(user.getEmail(), token.getConfirmationToken());
			//
			LOGGER.info("User informations has been Add seccessfully");
			return user;

		} catch (Exception e) {
			ResponseEntity.ok(new ApiError(false, "Error :" + e));
		}
		return null;

	}

	public boolean changePassword(User user, String password) {
		// boolean returnValue = false;

		/*
		 * if(user==null) {
		 * return returnValue;
		 * }
		 * 
		 * 
		 * 
		 * String token = Jwts.builder()
		 * .setSubject((user.getEmail()))
		 * .setIssuedAt(new Date())
		 * .setExpiration((Date) ((JwtBuilder) new
		 * Date(System.currentTimeMillis()+600000)))
		 * .signWith(SignatureAlgorithm.HS512,"clinitalSecretKey")
		 * .compact();
		 */
		user.setPassword(encoder.encode(password));
		if (userRepository.save(user) != null) {
			return true;
		}
		return false;
	}

	public boolean BlockUser(long id) {
		User user = userRepository.getById(id);
		if (user != null && user.getRole() != ERole.ROLE_ADMIN) {
			user.setEnabled(!user.isEnabled());

		} else {
			return false;
		}

		if (userRepository.save(user) != null) {
			return true;
		}
		return false;

	}

	// Manage Visibility of a doctor :
	public boolean ShowMedecin(long id) {
		Medecin med = medRepository.getById(id);
		if (med != null) {
			med.setIsActive(!med.getIsActive());

		} else {
			return false;
		}

		if (medRepository.save(med) != null) {
			return true;
		}
		return false;

	}

	public ResponseEntity<?> RegistreNewUser(SignupRequest signUpRequest) throws Exception {

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// User user1 =
		// userRepository.findUserByEmail(signUpRequest.getEmail()).orElseThrow(()->new
		// Exception("Error: Email is already in use!"));
		// Create new user's account
		User user = new User(signUpRequest.getEmail(), signUpRequest.getTelephone(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getRole());

		user.setProvider(ProviderEnum.LOCAL);
		// save user

		entityManger.persist(user);
		// userRepository.save(user);
		this.save(user,null);

		// save activity of user signup
		activityServices.createActivity(new Date(), "add", "signup seccussefuly done", user);
		LOGGER.info("Inscription reussi");
		return ResponseEntity.ok(user);

	}

}
