package com.clinital.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.flow.ReturnNode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.Null;

import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.function.EntityResponse;

import com.clinital.dao.IDao;
import com.clinital.dto.RendezvousDTO;
import com.clinital.enums.ERole;
import com.clinital.exception.BadRequestException;
import com.clinital.models.Cabinet;
import com.clinital.models.Document;
import com.clinital.models.Medecin;
import com.clinital.models.ModeConsultation;
import com.clinital.models.MotifConsultation;
import com.clinital.models.Patient;
import com.clinital.models.Rendezvous;
import com.clinital.models.Specialite;
import com.clinital.models.User;
import com.clinital.payload.request.RendezvousRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.repository.CabinetRepository;
import com.clinital.repository.DocumentRepository;
import com.clinital.repository.ModeConsultRespository;
import com.clinital.repository.MotifCondultationRepository;
import com.clinital.repository.PatientRepository;
import com.clinital.repository.RdvRepository;
import com.clinital.repository.SpecialiteRepository;
import com.clinital.security.config.VideoCall.UrlVideoCallGenerator;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.util.ApiError;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;
@Service
@Transactional(propagation = Propagation.REQUIRED)
//@Primary
public class RendezvousService implements IDao<Rendezvous> {

	@Autowired
	private RdvRepository rdvrepo;

	 @Autowired
	 private DocumentRepository docrepo;
	 @Autowired
	 private PatientRepository patientRepo;
	 @Autowired
	 private ModeConsultRespository moderespo;
	 @PersistenceContext
	private EntityManager entityManger;
	@Autowired
	private SpecialiteRepository specialiteRepository;
	@Autowired
	private MotifCondultationRepository mRepository;
	@Autowired
	private CabinetRepository cabrepo;
	@Autowired
	private ClinitalModelMapper mapper;

	@Autowired
	private ActivityServices ActivityServices;
	@Autowired
	private GlobalVariables globalVariables;
	private final Logger LOGGER=LoggerFactory.getLogger(getClass());
	@Autowired
	private UrlVideoCallGenerator urlVideoCallGenerator;
	@Override
	public Rendezvous create(Rendezvous o) {
		return rdvrepo.save(o);

	}

	@Override
	public void update(Rendezvous o) {
		rdvrepo.save(o);
	}

	@Override
	public void delete(Rendezvous o) {
		rdvrepo.delete(o);

	}

	public void deleteRendezvous(Long idrdv) throws Exception {
        
        
        Optional<Rendezvous> rdv = rdvrepo.getRendezvousById(idrdv);

		

        if (rdv.isPresent()){
			List<Document> docs = docrepo.getDocByIdRendezvous(idrdv);

			docs.forEach(doc -> doc.setRendezvous(null));
			
            rdvrepo.DeletRdvByIdProfile(idrdv);
            
        }
        else
            {
               
                throw new Exception("Fail to delete");
                
            }
       
        
    }

	public void UpdateRdvByIdPatient(RendezvousRequest req,long id) throws Exception{
		DayOfWeek day = DayOfWeek.valueOf(req.getDay());
		rdvrepo.UpdateRdvByIdProfile(day,req.getStart(),req.getStatut().toString(),req.getMedecinid(),req.getPatientid(),req.getMotif().toString(),req.getEnd(), id,req.getModeconsultation());
		
	}

	public void UpdateRdvByIdMedecin(RendezvousRequest req,long id,long idMed) throws Exception{
		DayOfWeek day = DayOfWeek.valueOf(req.getDay());
		
		rdvrepo.UpdateRdvByIdProfile(day,req.getStart(),req.getStatut().toString(),idMed,req.getPatientid(),req.getMotif().toString(),req.getEnd(), id,req.getModeconsultation());
	
	}

	public ResponseEntity<?> UpdateRdvdate(RendezvousRequest req,long id) throws Exception{
		//DayOfWeek day = DayOfWeek.valueOf(req.getDay());

		DayOfWeek day = req.getStart().getDayOfWeek();
		Optional<Rendezvous> isrdv=rdvrepo.IsRdvinDateStart(req.getStart(),day.getValue()-1,req.getMedecinid());
		if(isrdv.isPresent()){

			ActivityServices.createActivity(new Date(), "Update", "You Cant Change this Rdv : "+id,
					globalVariables.getConnectedUser());
		LOGGER.info("You Cant Change this Rdv , UserID : " + globalVariables.getConnectedUser().getId());
			
			return ResponseEntity.accepted().body(new ApiError(HttpStatus.FORBIDDEN, "You cant update this date", null));
			
		} else {
			ActivityServices.createActivity(new Date(), "Update", "Update Rdv for Patient",
					globalVariables.getConnectedUser());
		LOGGER.info("Update Rdv for Patient, UserID : " + globalVariables.getConnectedUser().getId());
			rdvrepo.UpdateRdvdatestart(day, req.getStart(), req.getEnd(), id);
			return ResponseEntity.ok(rdvrepo.getById(id)) ;
		}

	}

	@Override
	public List<Rendezvous> findAll() {
		return rdvrepo.findAll();
	}

	@Override
	public Optional<Rendezvous> findById(long id) {
		return rdvrepo.findById(id);
	}

	public Optional<Rendezvous> getRendezvousById(long id){
		return rdvrepo.getRendezvousById(id);
	}

	public List<Rendezvous> findRendezvousByMedAndDate(Long medecinId, LocalDateTime date) {
//		ZoneId systemTimeZone = ZoneId.systemDefault();

//		ZonedDateTime zonedDateTimeFrom = fromDate.atStartOfDay(systemTimeZone);
//		ZonedDateTime zonedDateTimeTo = toDate.atStartOfDay(systemTimeZone);
//
//		Date startDate = Date.from(zonedDateTimeFrom.toInstant());
//		Date endDate = Date.from(zonedDateTimeTo.toInstant());

		return rdvrepo.findByDateAndMedecin(date.toLocalDate(), medecinId).stream()
		.map(rdv -> mapper.map(rdv, Rendezvous.class)).collect(Collectors.toList());

	}
	public List<Rendezvous> findRendezvousByPatientAndDate(Long patId, LocalDateTime fromDate) {
				return rdvrepo.findByDateAndPatient(fromDate, patId).stream()
						.map(rdv -> mapper.map(rdv, Rendezvous.class)).collect(Collectors.toList());
		
			}
		

	
//RDV BY DATE (DAR,WEEK,MONTH,YEAR) For A DOCTOR :
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvMedByDay(@Valid long day,long id){
		return rdvrepo.getRendezvousMedByDay(day,id);
	}
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvMedByWeek(long week,long id){
		return rdvrepo.getRendezvousMedByWeek(week,id);
	}
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvMedByMonth(long month,long id){
		return rdvrepo.getRendezvousMedByMonth(month,id);
	}
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvMedByYear(long year,long id){
		return rdvrepo.getRendezvousMedByYear(year,id);
	}
	
//RDV BY DATE (DAR,WEEK,MONTH,YEAR) For A Patient :

//Get Rdv By Day of Week : 
public List<Rendezvous> getRdvPatientByDayWeek(long day,long id){
	return rdvrepo.getRendezvousPatientByDayofweek(day,id);
}
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvPatientByDay(long day,long id){
		return rdvrepo.getRendezvousPatientByDay(day,id);
	}
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvPatientByWeek(long week,long id){
		return rdvrepo.getRendezvousPatientByWeek(week,id);
	}
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvPatientByMonth(long month,long id){
		return rdvrepo.getRendezvousPatientByMonth(month,id);
	}
	//Get Rdv By Day : 
	public List<Rendezvous> getRdvPatientByYear(long year,long id){
		return rdvrepo.getRendezvousPatientByYear(year,id);
	}

	//RDV by Id User and Patient ID :
	 public List<Rendezvous> findRdvByIdUserandPatient(long iduser,long idpatient){
		return rdvrepo.findRdvByIduserandPatient(iduser, idpatient)
		.stream().map(pat->mapper.map(pat, Rendezvous.class))
		.collect(Collectors.toList());

	 }

	//RDV by Id and Patient ID :
	public Rendezvous findRdvByIdUserandId(long iduser,long id){
				return rdvrepo.findRdvByIdUserandId(iduser, id);

	 }

	public List<Rendezvous> getRdvByIdMedecinandIdPatient(long idmed,long patid){
		return rdvrepo.getRdvByIdMedecinandIdPatientandDate(idmed,patid);
	}


	// Add RDV :
	 public ResponseEntity<?> AddnewRdv(User user,RendezvousDTO c,Medecin medecin,Patient patient) throws Exception{
		
		try {	
		// DayOfWeek day = DayOfWeek.valueOf(c.getDay());
	MotifConsultation motif = mRepository.findById(c.getCabinet()).orElseThrow(()->new Exception("No such Id exist for a Motif"));
	ModeConsultation mode =moderespo.findById(c.getCabinet()).orElseThrow(()->new Exception("No such Id exist for a Mode consultation"));
	Cabinet cabinet=cabrepo.findById(c.getCabinet()).orElseThrow(()->new Exception("No such Id exist for a cabinet"));
	Boolean isReserved = false,ModeMedecin=false;
	isReserved=	this.isHasRdvToday(patient.getId(), medecin.getSpecialite().getId_spec(), c.getStart().toLocalDate());
	ModeMedecin=isReserved?false:true;
	
	if (!isReserved||ModeMedecin) {

		// DayOfWeek day = DayOfWeek.valueOf(c.getDay());
		Rendezvous rendezvous = new Rendezvous();
		rendezvous.setMedecin(medecin);
		rendezvous.setMotifConsultation(motif);
		rendezvous.setPatient(patient);
		rendezvous.setDay(c.getDay());
		rendezvous.setStart(c.getStart());
		rendezvous.setEnd(c.getEnd());
		rendezvous.setStatut(c.getStatut());
		rendezvous.setCanceledAt(c.getCanceledat());
		rendezvous.setModeConsultation(mode);
		rendezvous.setISnewPatient(c.getIsnewpatient());
		if (user != null && rendezvous != null) {
			ERole userRole = user.getRole();
			if (userRole == ERole.ROLE_MEDECIN && c != null && c.getCommantaire() != null) {
				rendezvous.setCommantaire(c.getCommantaire());
			}
		}
		rendezvous.setLinkVideoCall(urlVideoCallGenerator.joinConference());
		rendezvous.setCabinet(cabinet);
		//rdvrepo.save(rendezvous);
		entityManger.persist(rendezvous);
		ActivityServices.createActivity(new Date(),"Add","Add New Rdv ",user);
		LOGGER.info("Add new Rdv, UserID : "+user.getId());
		return ResponseEntity.ok(mapper.map(rendezvous, Rendezvous.class));

	} else
		return ResponseEntity.ok(new ApiResponse(false, "You have already an other RDV with "));

		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	 }

	// Checking if a patient has a rendezvous with a doctor today.
	public Boolean isHasRdvToday(Long idpat,Long spec,LocalDate date) throws Exception{
		try {
			 List<Rendezvous> rdv= rdvrepo.findRdvByPatientandSpecInDate(idpat, spec, date);
		
		if(rdv.isEmpty()){
			return false;
		} else return true;
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
		
	}
 
}
