package com.clinital.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clinital.dto.DemandeDTO;
import com.clinital.enums.DemandeStateEnum;
import com.clinital.enums.ERole;
import com.clinital.models.Demande;
import com.clinital.models.Rendezvous;
import com.clinital.models.User;
import com.clinital.payload.request.SignupRequest;
import com.clinital.payload.response.MessageResponse;
import com.clinital.repository.IDemandeRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.RdvRepository;
import com.clinital.repository.UserRepository;
import com.clinital.services.interfaces.DemandeService;
import com.clinital.util.ApiError;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;

import net.andreinc.mockneat.MockNeat;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.types.enums.StringType.*;
@Transactional
@Service
public class DemandeServiceImpl implements DemandeService{
	
	@Autowired
	private IDemandeRepository demandeRepository;
	
	@Autowired
	private MedecinRepository medecinRepository;
	
	@Autowired
	private ClinitalModelMapper modelMapper;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ActivityServices activityServices;

	@Autowired
	private GlobalVariables globalVariables;

	@Autowired
	UserServiceImpl userservice;
	@Autowired
	UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManger;

	public final Logger LOGGER=LoggerFactory.getLogger(this.getClass());

	@Override
	public ResponseEntity<?> create(DemandeDTO demande) {

		if (userRepository.existsByEmail(demande.getMail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		Demande d = modelMapper.map(demande,Demande.class);
		Demande saved = demandeRepository.save(d);
		emailSenderService.sendMailDemandeconfirmation(saved);
		
		//activityServices.createActivity(new Date(),"Add","Add New  Demande ID:"+saved.getId()+" By Connected Medecin Admin",globalVariables.getConnectedUser());
        LOGGER.info("Demande d'inscription cree par un Medecin son email : "+d.getMail());
		return ResponseEntity.ok(modelMapper.map(saved, Demande.class));
	}

	@Override
	public DemandeDTO update(DemandeDTO demande, Long id) throws Exception {
		Optional<Demande> demandeOptional = demandeRepository.findByIDemande(id);
		if(demandeOptional.isPresent()) {
			Demande demande1 = modelMapper.map(demande, Demande.class);
			demande1.setId(id);
			Demande updated = demandeRepository.save(demande1);
		
        	LOGGER.info("Modification Demande  cree par un Medecin son email "+demande1.getMail());
			return modelMapper.map(updated, DemandeDTO.class);
		}else
			throw new Exception("Failed to update");
	}

	@Override
	public List<DemandeDTO> findAll() {
		LOGGER.info("Admin consult All Demandes");
		return demandeRepository.findAll()
				.stream()
				.map(dm->modelMapper.map(dm, DemandeDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public DemandeDTO findById(Long id) throws Exception { 
				Demande demande=demandeRepository.findById(id).orElseThrow(()->new Exception("NO matching Found"));
				LOGGER.info("Consult Demande By ID : "+id);
		return modelMapper.map(demande, DemandeDTO.class);
	}

	@Override
	public void deleteById(Long id) throws Exception {
		Optional<Demande> demande = demandeRepository.findByIDemande(id);
		
		if(demande.isPresent()) {
			LOGGER.info("Demande  has been Deleted : "+demande.get().getMail());
			demandeRepository.deleteById(id);
		}
		else
			throw new Exception("Demande not found");
		
	}

	@Override
	public Demande validate(DemandeStateEnum valide, Long id) throws Exception {
		
		try {
			
			Optional<Demande> demandeOptional = demandeRepository.findByIDemande(id);
		if(demandeOptional.isPresent()) {

			Demande demande = demandeOptional.get();
			
			demande.setValidation(valide);
			Demande updated = demandeRepository.save(demande);
			LOGGER.info("Demande  has been Validated email : "+demande.getMail());
			if(valide==DemandeStateEnum.VALIDER){
				
				String pw=this.SecretCode();
				User user=new User();
				user.setEmail(demande.getMail());
				user.setPassword(passwordEncoder.encode(pw));
				user.setTelephone("0600000000");
				user.setRole(ERole.ROLE_MEDECIN);
				userRepository.save(user);
				LOGGER.info("New User is Created, Email : "+demande.getMail());
				
				User registred = userRepository.findById(user.getId()).orElseThrow(()->new Exception("this User is not found !"));
				userservice.save(registred,demande);
				emailSenderService.sendMailDemande(demande,pw);
				System.out.println(registred.getId());

			}
			
			
			return modelMapper.map(updated, Demande.class);
		}
			
		} catch (Exception e) {
			// TODO: handle exception
			ResponseEntity.ok(new ApiError(false, " ERROR :"+e));
		}
		return null;
		
	}

	@Override
	public List<Demande> findByState(DemandeStateEnum state) {
		// TODO Auto-generated method stub
		List<Demande> dmnd = demandeRepository.getdemandeByState(state.toString());
		LOGGER.info("Consulting Demandes by it State ");
		return dmnd;
	}

	// @Override
	// public DemandeDTO findById(Long id) throws Exception {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }

	// @Override
	// public void deleteById(Long id) throws Exception {
	// 	// TODO Auto-generated method stub
		
	// }

	

		public String SecretCode(){

				//MockNeat mock = MockNeat.secure();
				//String Email = mock.emails().domain("clinital.co").val();
				//String pass=mock.passwords().sha256().toString();
				 String code = strings().size(10).types(ALPHA_NUMERIC, HEX).get();
				 return code;
		}

	

}
