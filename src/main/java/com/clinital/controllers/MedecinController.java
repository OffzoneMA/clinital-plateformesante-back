package com.clinital.controllers;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.clinital.dto.*;
import com.clinital.models.Patient;
import com.clinital.models.Rendezvous;
import com.clinital.models.Secretaire;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clinital.exception.BadRequestException;
import com.clinital.models.Cabinet;
import com.clinital.models.CabinetMedecinsSpace;
import com.clinital.models.Consultation;
import com.clinital.models.DocumentsCabinet;
import com.clinital.models.DossierMedical;
import com.clinital.models.Medecin;
import com.clinital.models.MedecinNetwork;
import com.clinital.models.MedecinSchedule;
import com.clinital.models.Ordonnance;
import com.clinital.models.TypeConsultation;
import com.clinital.models.User;
import com.clinital.payload.request.CabinetRequest;
import com.clinital.payload.request.ConsultationRequest;
import com.clinital.payload.request.DocumentsCabinetRequest;
import com.clinital.payload.request.GetDossierRequest;
import com.clinital.payload.request.MedecinRequest;
import com.clinital.payload.request.OrdonnanceRequest;
import com.clinital.payload.request.SecritaireRequest;
import com.clinital.payload.request.SignupRequest;
import com.clinital.payload.request.networkRequest;
import com.clinital.payload.response.AgendaResponse;
import com.clinital.payload.response.ApiResponse;
import com.clinital.payload.response.FichePatientResponse;
import com.clinital.payload.response.GeneralResponse;
import com.clinital.payload.response.HorairesResponse;
import com.clinital.payload.response.MedecinResponse;
import com.clinital.payload.response.TypeConsultationResponse;
import com.clinital.repository.CabinetMedecinRepository;
import com.clinital.repository.CabinetRepository;
import com.clinital.repository.ConsultationRepository;
import com.clinital.repository.DocumentsCabinetRepository;
import com.clinital.repository.MedecinNetworkRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.MedecinScheduleRepository;
import com.clinital.repository.OrdonnanceRepository;
import com.clinital.repository.SecretaireRepository;
import com.clinital.repository.TypeConsultationRepository;
import com.clinital.security.config.azure.AzureServices;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.ActivityServices;
import com.clinital.services.CabinetMedecinServiceImpl;
import com.clinital.services.CabinetServiceImpl;
import com.clinital.services.ConsultationServices;
import com.clinital.services.DocumentsCabinetServices;
import com.clinital.services.MedecinNetworkService;
import com.clinital.services.MedecinScheduleServiceImpl;
import com.clinital.services.MedecinServiceImpl;
import com.clinital.services.OrdonnanceServiceImpl;
import com.clinital.services.PatientService;
import com.clinital.services.RendezvousService;
import com.clinital.services.SecretaireServiceImpl;
import com.clinital.services.UserServiceImpl;
import com.clinital.services.interfaces.SpecialiteService;
import com.clinital.util.ApiError;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;
import com.clinital.util.PDFGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/med/")
@Slf4j
public class MedecinController {

	@Autowired
	MedecinRepository medrepository;

	@Autowired
	ClinitalModelMapper mapper;

	@Autowired
	RendezvousService rendezvousService;

	@Autowired
	MedecinScheduleRepository medScheduleRepo;

	@Autowired
	TypeConsultationRepository typeConsultationRepo;

	@Autowired
	private MedecinServiceImpl medecinService;

	@Autowired
	private SpecialiteService specialiteService;

	@Autowired
	private MedecinNetworkService medecinNetworkService;

	@Autowired
	private MedecinNetworkRepository mednetRepository;

	@Autowired
	private PatientService patservices;

	@Autowired
	private CabinetMedecinRepository cabmedrep;

	private MedecinNetwork Follower;

	@Autowired
	private CabinetServiceImpl cabservice;

	@Autowired
	private CabinetMedecinServiceImpl medcabinetservice;

	@Autowired
	private CabinetRepository cabrepos;

	@Autowired
	private SecretaireServiceImpl secreServiceImpl;

	@Autowired
	private SecretaireRepository secretaireRepository;

	@Autowired
	private UserServiceImpl userservice;

	@Autowired
	private DocumentsCabinetRepository doccabrepository;

	public static boolean checkday = false;

	@Autowired
   	private AzureServices azureAdapter;

	@Autowired
	private DocumentsCabinetServices docservices;

	@Autowired
	private PatientService patientService;

	@Autowired
	private ConsultationServices consultationServices;

	@Autowired
    private ConsultationRepository consulrepo;

	@Autowired
	private OrdonnanceServiceImpl OrdonnanceServices;

	@Autowired
	private OrdonnanceRepository ordonnanceRepository;

	@Autowired
    GlobalVariables globalVariables;

	@Autowired
	private PDFGenerator pdfgenerator;

	@Autowired 
	private ActivityServices activityServices;

	private final Logger LOGGER=LoggerFactory.getLogger(getClass());
	// Get all medecins ... : %OK%

	@GetMapping("/medecins")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	public Iterable<Medecin> medecins() {

		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","Consult All Medecins",globalVariables.getConnectedUser());
		LOGGER.info("Consulting All Medecins by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		}
		
		return medrepository.findAll().stream().filter(med->med.getIsActive()==true).collect(Collectors.toList());
		// return medrepository.findAll().stream().map(med -> mapper.map(med, Medecin.class))
		// 		.collect(Collectors.toList());
	}

	// Get Medecin By Id : %OK%
	@GetMapping("/medById/{id}")
	public ResponseEntity<Medecin> getMedecinById(@PathVariable(value="id") Long id) throws Exception {
		
		return ResponseEntity.ok(mapper.map(medecinService.findById(id), Medecin.class));
	}

	// Get Connectde Medecin : %OK%
	@GetMapping("/myaccount")
	public ResponseEntity<Medecin> getMyMedecinAccount() throws Exception {

		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		activityServices.createActivity(new Date(),"Read","Consulting personnal Account ",globalVariables.getConnectedUser());
		LOGGER.info("Consult Personal Account Medecin , User ID: "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		return ResponseEntity.ok(med);
	}

	// Get Medecin y his name : %OK%
	@GetMapping("/medByName")
	@ResponseBody
	public Iterable<Medecin> findMedByName(@RequestParam String nomMed) {
		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","Consult Medecin By Name : "+nomMed,globalVariables.getConnectedUser());
			LOGGER.info("Consult Medecin By Name : "+nomMed+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		}
		return medrepository.getMedecinByName(nomMed).stream().filter(med->med.getIsActive()==true).collect(Collectors.toList());
	}

	// end point for getting Doctor by Name or speciality and city : %OK%
	@GetMapping("/medByNameOrSpecAndVille")
	@ResponseBody
	public Iterable<Medecin> medByNameOrSpecAndVille(@RequestParam String ville,
			@RequestParam String search) {
				if(globalVariables.getConnectedUser()!=null){
					activityServices.createActivity(new Date(),"Read","Consult Medecin By Ville : "+ville+" and Name/spcialite : "+search,globalVariables.getConnectedUser());
					LOGGER.info("Consult Medecin By Ville : "+ville+" and Name/spcialite : "+search+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
				}
		return medrepository.getMedecinBySpecialiteOrNameAndVille(ville, search).stream()
		.filter(med->med.getIsActive()==true).collect(Collectors.toList());
	}

	// end point for getting Doctor by Name and speciality : %OK%
	@GetMapping("/medByNameAndSpec")
	public Iterable<Medecin> findMedSpecNameVille(@RequestParam String name,
			@RequestParam String search) {
				if(globalVariables.getConnectedUser()!=null){
					activityServices.createActivity(new Date(),"Read","Consult Medecin By name : "+name+" and spcialite : "+search,globalVariables.getConnectedUser());
				LOGGER.info("Consult Medecin By name : "+name+" and spcialite : "+search+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
				}
		return medrepository.getMedecinBySpecialiteOrName(name, search).stream()
		.filter(med->med.getIsActive()==true).collect(Collectors.toList());
	}

	// end point for getting Doctor by Name or speciality : %OK%
	@GetMapping("/medByNameOrSpec")
	public Iterable<Medecin> findMedSpecName(@RequestParam String search) {
		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","Consult Medecin By name or spcialite : "+search,globalVariables.getConnectedUser());
		LOGGER.info("Consult Medecin By name or spcialite : "+search+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		}
		return medrepository.getMedecinBySpecOrName(search).stream().filter(med->med.getIsActive()==true).collect(Collectors.toList());

	}

	// end point for getting Doctor By city : %OK%
	@GetMapping("/medByVille")
	public Iterable<Medecin> findMedByVille(@RequestParam Long id_ville) {
		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","Consult Medecin By name ville ID"+id_ville,globalVariables.getConnectedUser());
		LOGGER.info("Consult Medecin By ville ID : "+id_ville+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		}
		return medrepository.getMedecinByVille(id_ville).stream().filter(med->med.getIsActive()==true).collect(Collectors.toList());

	}

	// Finding all the schedules bY med Id from a given date.%OK%
	@GetMapping("/schedulesofMed/{idmed}")
	@JsonSerialize(using = LocalDateSerializer.class)
	public List<MedecinSchedule> findallSchudelesfromDate(@PathVariable Long idmed,
			@PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "startDate", example = "yyyy-MM-dd") LocalDate startDate) {
				if(globalVariables.getConnectedUser()!=null){
					activityServices.createActivity(new Date(),"Read","Consult Schedules of Medecin by is ID: "+idmed,globalVariables.getConnectedUser());
				LOGGER.info("Consult schedules of Medecin By his ID : "+idmed+" name by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
				}
		return medScheduleRepo
				.findByMedId(idmed)
				.stream()
				.map(item -> mapper.map(item, MedecinSchedule.class))
				.collect(Collectors.toList());

	}

	// get agenda bY med Id from a given date.%OK%
	@GetMapping("/agenda/{idmed}/{weeks}/{startDate}")
	@JsonSerialize(using = LocalDateSerializer.class)
	public List<AgendaResponse> GetCreno(@Valid @PathVariable long idmed, @PathVariable long weeks,

			@PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "startDate", example = "yyyy-MM-dd") LocalDate startDate)
			throws Exception {

		try {

			List<AgendaResponse> agendaResponseList = new ArrayList<AgendaResponse>();
			List<MedecinSchedule> schedules = medScheduleRepo
					.findByMedId(idmed)
					.stream()
					.map(item -> mapper.map(item, MedecinSchedule.class))
					.collect(Collectors.toList());

					

			int days = medecinService.getDaysInMonth(startDate.atStartOfDay());

			for (int j = 1; j <= weeks; j++) {

				for (int i = 1; i <= 7; i++) {
					checkday = false;
					if (!schedules.isEmpty()) {
						for (MedecinSchedule medsch : schedules) {

							if (medsch.getDay().getValue() == startDate.getDayOfWeek().getValue()) {
								checkday = true;
								AgendaResponse agenda = new AgendaResponse();
								for (AgendaResponse ag : agendaResponseList) {
									if (ag.getDay().getValue() == medsch.getDay().getValue() && ag.getWeek() == j) {
										int index = agendaResponseList.indexOf(ag);
										agenda = agendaResponseList.get(index);
										agenda = medecinService.CreateCreno(medsch, agenda, idmed, j,
												startDate.atStartOfDay());
										agendaResponseList.set(index, agenda);

									}
								}
								agenda = medecinService.CreateCreno(medsch, agenda, idmed, j, startDate.atStartOfDay());
								// diffrance hourse :
								long Hours = ChronoUnit.HOURS.between(medsch.getAvailabilityStart(),
										medsch.getAvailabilityEnd());
								agenda.getMedecinTimeTable().add(new GeneralResponse("startTime",
										medsch.getAvailabilityStart()));
								agenda.getMedecinTimeTable().add(new GeneralResponse("endTime",
										medsch.getAvailabilityStart().plusHours(Hours)));
								String startTime = medsch.getAvailabilityStart().getHour() + ":"
										+ medsch.getAvailabilityStart().getMinute();

								String endTime = medsch.getAvailabilityEnd().getHour() + ":"
										+ medsch.getAvailabilityEnd().getMinute();

								agenda.getWorkingHours().add(new HorairesResponse(startTime,
										endTime));

								agendaResponseList.add(agenda);

								continue;

							}

						}
					}
					if (!checkday) {

						AgendaResponse agenda = new AgendaResponse();
						agenda.setDay(startDate.getDayOfWeek());
						agenda.setWorkingDate(startDate.atStartOfDay());
						agendaResponseList.add(agenda);
					}
					startDate = startDate.plusDays(1);//

				}

			}
			// Create a new LinkedHashSet
			Set<AgendaResponse> set = new LinkedHashSet<>();

			// Add the elements to set
			set.addAll(agendaResponseList);

			// Clear the list
			agendaResponseList.clear();

			// add the elements of set
			// with no duplicates to the list
			agendaResponseList.addAll(set);

			if(globalVariables.getConnectedUser()!=null){
				activityServices.createActivity(new Date(),"Read","Consult Medecin Agenda by his ID : "+idmed,globalVariables.getConnectedUser());
				LOGGER.info("Consult Medecin Agenda By his ID : "+idmed+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
			}

			return agendaResponseList;

		} catch (Exception e) {
			throw new BadRequestException("error :" + e);
		}

	}

	// Finding all the RDV bY med Id from a given date.%OK%
	@GetMapping("/rdvofMed/{idmed}/{startDate}")
	@JsonSerialize(using = LocalDateSerializer.class)
	public ResponseEntity<RendezvousDTO> findallRDVforMedBystartDate(@PathVariable Long idmed,
			@PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "startDate", example = "yyyy-MM-dd") LocalDate startDate) {

		LocalDateTime startDateTime = startDate.atStartOfDay();
		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","Consult Medecin RDV By his ID : "+idmed,globalVariables.getConnectedUser());
			LOGGER.info("Consult Medecin RDV By his ID : "+idmed+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		}
		return ResponseEntity.ok(mapper.map(rendezvousService
				.findRendezvousByMedAndDate(idmed, startDateTime), RendezvousDTO.class));

	}

	//

	// end point for Agenda of a doctor : %OK%
	// @PostMapping("/agenda")
	// @JsonSerialize(using = LocalDateSerializer.class)
	// public List<AgendaResponse> findAvailableTimes(@RequestParam("idmed") Long
	// idmed,
	// @RequestParam("idconsult") Long idconsult,
	// @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso =
	// DateTimeFormat.ISO.DATE) @ApiParam(value = "startDate", example =
	// "yyyy-MM-dd") LocalDate startDate) {

	// List<AgendaResponse> agendaResponseList = new ArrayList<AgendaResponse>();

	// LocalDate nowDate = LocalDate.now();
	// if (startDate != null)
	// nowDate = startDate;
	// List<MedecinSchedule> medecinSchedule = medScheduleRepo
	// .findByMedIdConsultationIdDate(idmed, nowDate);
	// try {
	// // TypeConsultation typeConsultation =
	// // typeConsultationRepo.findById(idconsult).get();

	// for (int i = 1; i <= 7; i++) {
	// AgendaResponse agendaResponse = new AgendaResponse();

	// agendaResponse.setDay(DayOfWeek.of(i));

	// for (MedecinSchedule schedule : medecinSchedule) {
	// if (schedule.getAvailabilityStart().getDayOfWeek().getValue() <= i) {
	// log.info("Found Day Greater than");

	// log.info("shedules is right here");
	// // List<Rendezvous> rendezvous =
	// // rendezvousService.findRendezvousByMedAndDate(idmed,
	// // schedule.getAvailabilityStart());

	// // for (Rendezvous rdv : rendezvous) {
	// // agendaResponse.setCanceledAt(rdv.getCanceledAt());
	// // }

	// long minutes = ChronoUnit.MINUTES.between(schedule.getAvailabilityStart(),
	// schedule.getAvailabilityEnd());

	// long totalSlots = minutes / schedule.getPeriod().getValue();

	// log.info("Total Slots " + totalSlots);
	// LocalDateTime timer = schedule.getAvailabilityStart();

	// if (startDate != null) {
	// timer = timer.of(startDate.getYear(), startDate.getMonth(),
	// startDate.getDayOfMonth(),
	// timer.getHour(), timer.getMinute());
	// }
	// log.info("Timer Start:" + timer);

	// for (int j = 0; j < totalSlots; j++) {
	// log.info(timer.toString());
	// Rendezvous rendezvous = rendezvousService.findRendezvousByMedAndDate(idmed,
	// timer);

	// if (rendezvous == null) {
	// agendaResponse.getAvailableSlot()
	// .add((timer.getHour() < 10 ? "0" : "") + timer.getHour() + ":"
	// + (timer.getMinute() < 10 ? "0" : "") + timer.getMinute());

	// } else {

	// if (rendezvous.getStart().getHour() == timer.getHour()
	// && rendezvous.getStart().getMinute() == timer.getMinute()) {
	// agendaResponse.getAvailableSlot()
	// .add("11:11");

	// }

	// }

	// timer = timer.plusMinutes(schedule.getPeriod().getValue());

	// }

	// } else {
	// log.info("Later Days");
	// }
	// }

	// // Getting Medecin General Time Table
	// // List<MedecinSchedule> medecinSchedule2 = medScheduleRepo
	// // .findByMedIdConsultationIdDate(idmed,nowDate);

	// // for (MedecinSchedule schedule : medecinSchedule) {

	// // // String date = "";
	// // // if (startDate != null) {
	// // // TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
	// // // date = startDate.with(fieldISO, i).toString();

	// // // } else {
	// // // TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
	// // // date = LocalDate.now().with(fieldISO, i).toString();

	// // // }

	// // // String startDateTime = date + "T" +
	// // schedule.getAvailabilityStart().getHour() + ":"
	// // // + schedule.getAvailabilityStart().getMinute();

	// // // String endDateTime = date + "T" +
	// schedule.getAvailabilityEnd().getHour()
	// // + ":"
	// // // + schedule.getAvailabilityEnd().getMinute();

	// // agendaResponse.getMedecinTimeTable().add(new GeneralResponse("startTime",
	// // schedule.getAvailabilityStart()));
	// // agendaResponse.getMedecinTimeTable().add(new GeneralResponse("endTime",
	// // schedule.getAvailabilityEnd()));

	// // String startTime = schedule.getAvailabilityStart().getHour() + ":"
	// // + schedule.getAvailabilityStart().getMinute();

	// // String endTime = schedule.getAvailabilityEnd().getHour() + ":"
	// // + schedule.getAvailabilityEnd().getMinute();

	// // agendaResponse.getWorkingHours().add(new HorairesResponse(startTime,
	// // endTime));

	// // }

	// agendaResponseList.add(agendaResponse);
	// }
	// } catch (Exception e) {
	// log.info("Warning mapping issue", e.getMessage());
	// }

	// return agendaResponseList;

	// }

	// end point for Agenda of a patient : %OK%
	// @PostMapping("/agendapatient")
	// @JsonSerialize(using = LocalDateSerializer.class)
	// public List<AgendaResponse> findRDVschedules(@RequestParam("idmed") Long
	// idmed,
	// @RequestParam("idconsult") Long idconsult,
	// @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso =
	// DateTimeFormat.ISO.DATE) @ApiParam(value = "startDate", example =
	// "yyyy-MM-dd") LocalDate startDate) {
	// List<AgendaResponse> agendaResponseList = new ArrayList<AgendaResponse>();
	// try {

	// UserDetailsImpl userDetails = (UserDetailsImpl)
	// SecurityContextHolder.getContext().getAuthentication()
	// .getPrincipal();
	// List<Patient> patient =
	// patservices.findALLPatientByUserId(globalVariables.getConnectedUser().getId());
	// TypeConsultation typeConsultation =
	// typeConsultationRepo.findById(idconsult).get();

	// for (int i = 1; i <= 7; i++) {
	// AgendaResponse agendaResponse = new AgendaResponse();

	// agendaResponse.setDay(DayOfWeek.of(i));

	// LocalDate nowDate = LocalDate.now();
	// if (startDate != null)
	// nowDate = startDate;

	// if (nowDate.getDayOfWeek().getValue() <= i) {
	// log.info("Found Day Greater than");
	// List<MedecinSchedule> medecinSchedule = medScheduleRepo
	// .findByMedIdConsultationIdDate(idmed, startDate);

	// for (MedecinSchedule schedule : medecinSchedule) {
	// for (Patient pat : patient) {

	// long minutes = ChronoUnit.MINUTES.between(schedule.getAvailabilityStart(),
	// schedule.getAvailabilityEnd());

	// long totalSlots = minutes / schedule.getPeriod().getValue();

	// log.info("Total Slots " + totalSlots);
	// LocalDateTime timer = schedule.getAvailabilityStart();

	// if (startDate != null) {
	// timer = timer.of(startDate.getYear(), startDate.getMonth(),
	// startDate.getDayOfMonth(),
	// timer.getHour(), timer.getMinute());
	// }
	// log.info("Timer Start:" + timer);

	// for (int j = 0; j < totalSlots; j++) {
	// log.info(timer.toString());

	// List<Rendezvous> rendezvous =
	// rendezvousService.findRendezvousByPatientAndDate(
	// pat.getId(),
	// schedule.getAvailabilityStart());

	// if (!rendezvous.isEmpty()) {
	// for (Rendezvous rd : rendezvous) {

	// agendaResponse.getAvailableSlot()
	// .add((rd.getStart().getHour() < 10 ? "0" : "") + rd.getStart().getHour()
	// + ":" + (rd.getStart().getMinute() < 10 ? "0" : "")
	// + rd.getStart().getMinute());
	// }

	// }

	// timer = timer.plusMinutes(schedule.getPeriod().getValue());

	// }
	// }

	// }
	// {
	// log.info("Empty Day Schedule");
	// }

	// } else {
	// log.info("Later Days");
	// }

	// // Getting Medecin General Time Table
	// List<MedecinSchedule> medecinSchedule = medScheduleRepo
	// .findByMedIdConsultationIdDate(idmed, startDate);

	// for (MedecinSchedule schedule : medecinSchedule) {

	// String date = "";
	// if (startDate != null) {
	// TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
	// date = startDate.with(fieldISO, i).toString();

	// } else {
	// TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
	// date = LocalDate.now().with(fieldISO, i).toString();

	// }

	// String startDateTime = date + "T" + schedule.getAvailabilityStart().getHour()
	// + ":"
	// + schedule.getAvailabilityStart().getMinute();

	// String endDateTime = date + "T" + schedule.getAvailabilityEnd().getHour() +
	// ":"
	// + schedule.getAvailabilityEnd().getMinute();

	// agendaResponse.getMedecinTimeTable()
	// .add(new GeneralResponse("startTime", schedule.getAvailabilityStart()));
	// agendaResponse.getMedecinTimeTable()
	// .add(new GeneralResponse("endTime", schedule.getAvailabilityEnd()));

	// String startTime = schedule.getAvailabilityStart().getHour() + ":"
	// + schedule.getAvailabilityStart().getMinute();

	// String endTime = schedule.getAvailabilityEnd().getHour() + ":"
	// + schedule.getAvailabilityEnd().getMinute();

	// agendaResponse.getWorkingHours().add(new HorairesResponse(startTime,
	// endTime));

	// }

	// for (MedecinSchedule schedule : medecinSchedule) {

	// }

	// agendaResponseList.add(agendaResponse);
	// }
	// } catch (Exception e) {
	// log.info("Warning mapping issue", e.getMessage());
	// }

	// return agendaResponseList;

	// }

	// end point for getting get type Consultation of a doctor : %OK%
	@GetMapping("/getTypeConsultationById/{id}")
	@ResponseBody
	public List<TypeConsultationResponse> findTypeConsultationById(@PathVariable Long id) {

		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","Consult All Consultation type By Medexin ID : "+id,globalVariables.getConnectedUser());
		LOGGER.info("Consult All consultation type by Medecin ID : "+id+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		}

		return typeConsultationRepo.findAllByMedecinId(id).stream()
				.map(typeConsult -> mapper.map(typeConsult, TypeConsultationResponse.class))
				.collect(Collectors.toList());
	}

	@PostMapping("/addmedecin")
	public ResponseEntity<MedecinDTO> Create(@RequestBody MedecinRequest MedRequest) throws Exception {
		try {
			medecinService.create(MedRequest);
		return new ResponseEntity<MedecinDTO>(HttpStatus.CREATED);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		

	}

	@PutMapping("/updatemed/{id}")
	public ResponseEntity<MedecinDTO> update(@RequestBody MedecinRequest dto, @PathVariable Long id) throws Exception {

		MedecinDTO medecinDTO = medecinService.update(dto, id);

		return ResponseEntity.accepted().body(medecinDTO);
	}

	// end point for getting all Specialite : %OK%
	@GetMapping("/getAllSpec")
	public ResponseEntity<List<SpecialiteDTO>> findAll() {

		if(globalVariables.getConnectedUser()!=null){
			activityServices.createActivity(new Date(),"Read","Consult All specialite",globalVariables.getConnectedUser());
		LOGGER.info("Consult All specialites by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		}
		return ResponseEntity.ok(specialiteService.findAll());
	}

	/*
	 * PATIENT PART :
	 */
	// end point for getting all Patients of a doctor : %OK%
	@GetMapping("/getAllPatients")
	public ResponseEntity<List<PatientDTO>> findAllPatients() throws Exception {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		
		activityServices.createActivity(new Date(),"Read","Consult All Patient  By Connected Medecin ",globalVariables.getConnectedUser());
		LOGGER.info("Consult All Patient by Connected Medecin , User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		
		return ResponseEntity.ok(medecinService.getMedecinPatients(globalVariables.getConnectedUser().getId()).stream()
				.map(patient -> mapper.map(patient, PatientDTO.class))
				.collect(Collectors.toList()));
	}

	// end point for getting a Patient of a doctor : %OK%
	@GetMapping("/getPatient/{idpatient}")
	public ResponseEntity<PatientDTO> findPatient(@Valid @PathVariable Long idpatient) throws Exception {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		
		activityServices.createActivity(new Date(),"Read","Consult Patinet By ID : "+idpatient+"  for Connected Medecin ",globalVariables.getConnectedUser());
		LOGGER.info("Consult Patient by id "+idpatient+" for Medecin Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		
		return ResponseEntity.ok(medecinService.getPatient(globalVariables.getConnectedUser().getId(), idpatient));

	}

	/*
	 * NETWORK PART :
	 */
	// Add a New Doctor to the Network : %OK%
	@PostMapping("/addNewNetwork")
	public ResponseEntity<?> addNewNetwork(@Valid @RequestBody networkRequest network) {

		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();

		MedecinNetwork MedNet = medecinNetworkService.addMedecinNetwork(network, globalVariables.getConnectedUser().getId());
		// return new ResponseEntity<MedecinNetworkDTO>(HttpStatus.CREATED);
		// return ResponseEntity.ok(new ApiResponse(true, "Network has benn add
		// Seccussefully"));
		activityServices.createActivity(new Date(),"Add","Add Medecin By ID : "+network.getFollower_id()+"  for Connected Medecin Network",globalVariables.getConnectedUser());
		LOGGER.info("Add Medecin by id "+network.getFollower_id()+" for Medecin Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		return ResponseEntity.ok(mapper.map(MedNet, MedecinNetwork.class));

	}

	// Find a Medecin in a network : %OK%

	@GetMapping("/getMedNetWork/{follower_id}")
	public MedecinDTO getMedecinNetworkbyId(@Valid @PathVariable Long follower_id) throws Exception {

		// Getting the current user from the security context.
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin med = medrepository.getMedecinByUserId(globalVariables.getConnectedUser().getId());

		// return
		// ResponseEntity.ok(medecinNetworkService.getMedecinfollewerById(med.getId(),
		// follower_id));
		// return ResponseEntity.ok(
		// medecinNetworkService.getMedecinfollewerById(med.getId(), follower_id),
		// MedecinNetworkDTO.class);
		activityServices.createActivity(new Date(),"Read","Consulting Medecin Follower By ID : "+follower_id+"  for Connected Medecin Network",globalVariables.getConnectedUser());
		LOGGER.info("Consulting Medecin Follower by id "+follower_id+" for Medecin Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		return mapper.map(medecinNetworkService.getMedecinfollewerById(med.getId(), follower_id), MedecinDTO.class);

	}

	// Delete a Medecin from network : %OK%
	@DeleteMapping(path = "/deletNetwork/{follower_id}")
	public ResponseEntity<?> deleteMedecinNetwork(@Valid @PathVariable Long follower_id)
			throws Exception {
		// Getting the current user from the security context.
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin med = medrepository.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		

		medecinNetworkService.deleteMedecinNetwork(med.getId(), follower_id);

		return ResponseEntity.ok(new ApiResponse(true, "Deleted"));

	}

	// Show all Network of a doc : %OK%
	@GetMapping("/getMedNetWork")
	public ResponseEntity<List<MedecinDTO>> getAllMedecinNetwork() throws Exception {
		// Getting the current user from the security context.
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin med = medrepository.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		// List<Medecin> Followers =
		// medecinNetworkService.getAllMedecinNetwork(med.getId());
		activityServices.createActivity(new Date(),"Read","Consult Medecin  Network  for Connected Medecin Network",globalVariables.getConnectedUser());
        LOGGER.info("Consult Medecin Network for Medecin Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		return ResponseEntity.ok(medecinNetworkService.getAllMedecinNetwork(med.getId()).stream()
				.map(follower -> mapper.map(follower, MedecinDTO.class))
				.collect(Collectors.toList()));

	}

	//
	// Setting the visibility of the medication with the given id.
	@PostMapping("/setvisibilityMed/{id}")
	public ResponseEntity<?> Setvisibilty(@Valid @PathVariable long id) {

		Medecin med = medecinService.setVisibiltyMedecin(id);
		activityServices.createActivity(new Date(),"Update","Update Medecin Visibilte : "+id,globalVariables.getConnectedUser());
        LOGGER.info("Update Medecin visibelite by id :"+id +", Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		return ResponseEntity.ok(mapper.map(med, Medecin.class));

	}
	// Setting the visibility of the medication with the given id.
	@PostMapping("/setcabinetvalidation/{id}")
	public ResponseEntity<?> SetCabinetState(@Valid @PathVariable long id) throws Exception {

		Cabinet cabinet = cabservice.CabinetStateValidation(id);
		activityServices.createActivity(new Date(),"Update","Update Cabinet By ID : "+id+", Cabinet State Updated to :"+cabinet.getState(),globalVariables.getConnectedUser());
            LOGGER.info("Update Cabinet By ID : "+id+", Cabinet State Updated to :"+cabinet.getState()+" , by Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		return ResponseEntity.ok(mapper.map(cabinet, Cabinet.class));

	}

// CABINET API :
	// A POST request to the /addcabinet endpoint.
	@PostMapping("/addcabinet")
	public ResponseEntity<?> Addcabinet(@Valid @RequestBody CabinetRequest cabinetreq) throws Exception {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin medecin = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		Cabinet cabinet = cabservice.create(cabinetreq,medecin);
		cabinetreq.setId_cabinet(cabinet.getId_cabinet());
		medecin.setStepsValidation(medecin.getStepsValidation()+1);
		medrepository.save(medecin);
		CabinetMedecinsSpace Cabmed = medcabinetservice.addCabinetMedecinsSpace(cabinetreq.getCabinetmedecin(),cabinet,medecin );
		activityServices.createActivity(new Date(),"Add","Add new Cabinet By Connected Medecin Admin",globalVariables.getConnectedUser());
            LOGGER.info("Add new Cabinet ID: "+cabinet.getId_cabinet()+" , by Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		//return cabinet;
		return ResponseEntity.ok(cabinet);

	}

	// add An existing cabinet to Med :
	@PostMapping("/addMedtoExistcabinet")
	public Cabinet AddMedtoCabinet(@Valid @RequestBody CabinetRequest cabinetreq) throws Exception {
		Medecin medecin= medecinService.findById(cabinetreq.getCabinetmedecin().getMedecin_id());
		Cabinet cabinet=cabrepos.getOne(cabinetreq.getCabinetmedecin().getCabinet_id());
		medecin.setStepsValidation(medecin.getStepsValidation()+1);
		medrepository.save(medecin);
		CabinetMedecinsSpace Cabmed = medcabinetservice.addCabinetMedecinsSpace(cabinetreq.getCabinetmedecin(),cabinet, medecin);
		activityServices.createActivity(new Date(),"Add","Add a Medecin ID "+ medecin.getId()+" to  Cabinet By Connected Medecin Admin",globalVariables.getConnectedUser());
            LOGGER.info("Add Medecin ID "+ medecin.getId()+" to Cabinet ID: "+cabinet.getId_cabinet()+" , by Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
		return cabinet;

	}

// delet a cabinet :
	@DeleteMapping(path = "/deletecabinet/{id}")
	public ResponseEntity<?> DeleteCabinet(@Valid @PathVariable long id) throws Exception {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		Optional<CabinetMedecinsSpace> isAdminMed = cabmedrep.isAdmin(med.getId(), id);
		if (isAdminMed.isPresent()) {
			Cabinet cabinet = cabrepos.getById(id);
			medcabinetservice.deleteCabinetMedecins(id);
			cabrepos.DeleteCabinetbyID(id);

			// med.removeCabinet(cabinet);
			activityServices.createActivity(new Date(),"Delete","Add a  Cabinet ID:"+cabinet.getId_cabinet()+" By Connected Medecin Admin",globalVariables.getConnectedUser());
            LOGGER.info("Delete a Cabinet ID: "+cabinet.getId_cabinet()+" , by Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
			return ResponseEntity.ok("Cabinet has been deleted successefully");
		} else
			throw new BadRequestException("You are Not Allowed");

	}
// add docs to Cabinet :
	@PostMapping(path = "/addCabinetDoc")
	@ResponseBody
	public ResponseEntity<?> addCabinetDoc(@RequestParam String document,
			@RequestParam MultipartFile docFile) throws Exception {
		ObjectMapper om = new ObjectMapper();

		DocumentsCabinetRequest documentReq = om.readValue(document, DocumentsCabinetRequest.class);

		try {
			UserDetailsImpl CurrentUser= (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Medecin med= medrepository.getMedecinByUserId(CurrentUser.getId());
			//----Add document : 

			DocumentsCabinet savedDoc = docservices.create(documentReq);
			
			// --------------------- Initial doc Upload :
			String extension = FilenameUtils.getExtension(docFile.getOriginalFilename());
			String fileName = savedDoc.getId()+ "cabinet." + extension;
			String UploadedFile = azureAdapter.upload(docFile,fileName,"Cabinet");
			// --------------- retirve file nam e after Upload
			savedDoc.setFichier_doc(UploadedFile);
		
			doccabrepository.save(savedDoc);
			med.setStepsValidation(med.getStepsValidation()+1);
			medrepository.save(med);
			activityServices.createActivity(new Date(),"Add","Add New document ID:"+savedDoc.getId()+", for Cabinet ID : "+documentReq.getId_cabinet(),globalVariables.getConnectedUser());
			LOGGER.info("Add New document ID:"+savedDoc.getId()+", for Cabinet ID : "+documentReq.getId_cabinet()+" by User : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
			return ResponseEntity.ok(new ApiResponse(true, "Document created successfully!"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse(false, "Document not created!"+e.getMessage()));

		}

	}
	@GetMapping("/mycabinets")
	public ResponseEntity<?> AllCabinetByCurrentMedecin() throws Exception{
	
		Medecin med=medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		List<Cabinet> cabinets=cabservice.allCabinetsByMedID(med.getId());

		activityServices.createActivity(new Date(), "Read", "Consult all Cabinets where work this Medecin with ID "+med.getId(), globalVariables.getConnectedUser());
        LOGGER.info("Consult all Cabinets where work this Medecin with ID "+med.getId()+" , By User : "+globalVariables.getConnectedUser());

		return ResponseEntity.ok(cabinets) ;
	}

	// SERCETAIRE API :
	@DeleteMapping("/delsecret/{idcabinet}/{idsec}")
	public ResponseEntity<?> DeleteSecretaireFromCabinet(@PathVariable Long idcabinet,
			@PathVariable Long idsec) throws Exception {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		Optional<CabinetMedecinsSpace> isAdminMed = cabmedrep.isAdmin(med.getId(), idcabinet);
		if (isAdminMed.isPresent()) {

			if (secreServiceImpl.deleteSecretaireById(idsec, idcabinet)) {

				activityServices.createActivity(new Date(),"Delete","Delete Scretaire :"+idsec+", By Medecin Admin",globalVariables.getConnectedUser());
				LOGGER.info("Delete Secretaire :"+idsec+", By Medecin Admin, UserID : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));

				return ResponseEntity.ok(new ApiResponse(true, "Deleted Seccuesefully"));
			}
		} else
				activityServices.createActivity(new Date(),"Error","You are not Allowed to Delete",globalVariables.getConnectedUser());
				LOGGER.info("You are not Allowed to Delete, UserID : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
				throw new BadRequestException(" You are not Allowed !");
		

	}
	// Add an axistant SERCETAIRE  :
	@PostMapping("addSecretaireById/{idcabinet}/{idsec}")
	public ResponseEntity<?> AddExisttingSecret(@PathVariable Long idcabinet,
			@PathVariable(value = "idsec") long id, @RequestBody SecritaireRequest secretaire) throws Exception {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		Cabinet cabinet=cabrepos.findById(idcabinet).orElseThrow(()->new Exception("No Matching Found"));
		Optional<CabinetMedecinsSpace> isAdminMed = cabmedrep.isAdmin(med.getId(), cabinet.getId_cabinet());
		if (isAdminMed.isPresent()) {

			Secretaire sec = secretaireRepository.findById(id).orElseThrow(()->new Exception("No Matching Found"));
		
			// check if any Cabinet exist already in Relation with this Secretaire
			Optional<Cabinet> iscabinet = cabrepos.isCabinetSecret(sec.getId(), cabinet.getId_cabinet());
			// check if this cabinet exist for this secretaire yes skip else add it

			if (!iscabinet.isPresent()) {
				
				sec.getCabinet().add(cabinet);
				activityServices.createActivity(new Date(),"Add","Add New Secretaie ID: "+sec.getId()+" to Cabinet ID: "+cabinet.getId_cabinet(),globalVariables.getConnectedUser());
				LOGGER.info("Add New Secretaie ID : "+sec.getId()+" to Cabinet ID : "+cabinet.getId_cabinet()+", UserID : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
				return ResponseEntity.ok(sec);

	
			
		}else
		activityServices.createActivity(new Date(),"Warning","Secretaie ID: "+sec.getId()+" Not Found",globalVariables.getConnectedUser());
		LOGGER.warn("Secretaie ID : "+sec.getId()+" Not Found, UserID : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
			throw new Exception("Failed to Find Secretaire");
			

			
		} else
		activityServices.createActivity(new Date(),"Error","Your not Allowed to add Secretaire",globalVariables.getConnectedUser());
		LOGGER.warn("You are not Allowed to add Secretaire, UserID : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
			throw new BadRequestException(" You are not Allowed !");
		

	}

	// 
	
	// check if ther are any null or empty values :
		// check if ther are any null or empty values :
		@GetMapping("/checkvalidation")
		public ResponseEntity<?> CheckValues() throws Exception{
	
	
			// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
			// 		.getPrincipal();
			
			Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
			
			Long Steps = med.getStepsValidation();
			activityServices.createActivity(new Date(),"Read","Checking Steps Validation",globalVariables.getConnectedUser());
			LOGGER.info("Checking Steps Validation, UserID : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
			return ResponseEntity.ok(Steps);

			
			
			
	
		}
// DOCUMENTS PATIENTS :
		//check if the "Dossier Medical" is accessible or not :
	@GetMapping("/isaccessible/{iddoss}")
	public Boolean CheckAccessFolder(@PathVariable Long iddoss) throws Exception{
		return medecinService.isAccessibleFolder(iddoss);
	}

	//ask the patient permisson to get access to his "Dossier Medical":
	@GetMapping("/askpermission/{idpat}")
	public  ResponseEntity<?> AskPermissiontoAccesFolder(@PathVariable Long idpat) throws Exception{
		try {
			Patient patient= patientService.findById(idpat).orElseThrow(()->new Exception("No Matching Patient Found"));
			
			return ResponseEntity.ok(medecinService.Askpermission(patient));
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}

    @GetMapping("/getdossier")
	public ResponseEntity<?> GetDocuments(@RequestBody GetDossierRequest getdossierRequest) throws Exception, ApiError {
		try {
			return medecinService.GetaccessToFolder(getdossierRequest.getIddoss(),getdossierRequest.getCodeaccess());
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}
// Generate Fiche Patient %OK% :
    @GetMapping("/generatefichepatient/{idpat}")
	public ResponseEntity<?> gFichePatient(@PathVariable Long idpat ) throws Exception{

			UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
			//FichePatientResponse fiche = patientService.GenrateFichepatient(idpat, med);
			return ResponseEntity.ok(patientService.GenrateFichepatient(idpat, med));
		
	}
//UPLOAD PROFILE PICTURE :
//  :
@PostMapping(path = "/uploadprofilepicture")
@ResponseBody
public ResponseEntity<?> addProfilepicture(@RequestParam MultipartFile imgFile) throws Exception {
			
			try {
				//----Add document : 
		
		// ------ Save Doc 
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		// --------------------- Initial doc Upload :
		String extension = FilenameUtils.getExtension(imgFile.getOriginalFilename());
		String fileName = med.getMatricule_med()+"-"+ med.getId()+"-"+med.getNom_med()+"." + extension;
		String UploadedFile = azureAdapter.upload(imgFile,fileName,"ProfilePicture");
		// --------------- retirve file nam e after Upload
		med.setPhoto_med(UploadedFile);

		
		Medecin finalSavedDoc = medrepository.save(med);
		return ResponseEntity.ok(mapper.map(finalSavedDoc,MedecinDTO.class));
	} catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.ok(new ApiResponse(false, "Profile Picture not created! : "+e.getMessage()));

	}

//


}
// download a file :
@GetMapping("/download")
   public ResponseEntity<Resource> getFile
      (@RequestParam String fileName)
         throws URISyntaxException {

      ByteArrayResource resource =
            new ByteArrayResource(azureAdapter
         .getFile(fileName));

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\""
          + fileName + "\"");

      return ResponseEntity.ok().contentType(MediaType
                  .APPLICATION_OCTET_STREAM)
            .headers(headers).body(resource);
   }

	//----------------------------------------------------------------------------------------
   	// Consultation :
	// add new consultation :
	@PostMapping("/addconsultation")
	public ResponseEntity<?> AddNewConsultation(@Valid @RequestBody ConsultationRequest creq){
		
			try {
				UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				
				Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		
				Consultation consul=consultationServices.create(creq, med);
		
				return ResponseEntity.ok(mapper.map(consul,ConsultationDTO.class));
		
			} catch (Exception e) {
				e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
			}
			
		
		}
			
	//update consulation :
	@PostMapping("/updateconsultation")
	public ResponseEntity<?> Updateconsultation(@Valid @RequestBody ConsultationRequest creq){

		try {
			UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());

			ConsultationDTO consul=consultationServices.update(creq,med);

			return ResponseEntity.ok(consul);

		} catch (Exception e) {
			e.printStackTrace();
		return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
		}
		

	}
// Delete Coonsultation : 
@DeleteMapping(path = "/deleteconsult/{id}")
public ResponseEntity<?> DeleteConsulttation(@Valid @PathVariable Long id){
	try {
		Consultation consultation=consulrepo.findById(id).orElseThrow(()->new Exception("No 	matching found for this Consultation"));

		Boolean IsDeleted = consultationServices.deleteById(consultation)?true:false;

		return IsDeleted ? ResponseEntity.ok(new ApiResponse(true, "Consultatio has been deleted seccussefully")):ResponseEntity.ok(new ApiResponse(true, "Consultatio cannot be deleted"));

	} catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
	}

}

// show data by medecin access right

@GetMapping(path = "/findconsult/{id}")
public ResponseEntity<?> findByIdMedecin(@Valid @PathVariable(value = "id")Long idcons){

	try {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		ConsultationDTO consul=consultationServices.findByIdMedecin(med.getId(), idcons);

		return ResponseEntity.ok(consul);
	} catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
	}
	
	

}
@GetMapping(path = "/allconsult")
public ResponseEntity<?> findByIdMedecin(){

	try {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		List<ConsultationDTO> allconsul=consultationServices.findALLByIdMedecin(med.getId());

		return ResponseEntity.ok(allconsul);
	} catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));

	}
	

}

//=================================================================================================
// ORDONNANCE :


@PostMapping("/addordonnance")
	public ResponseEntity<?> AddNewOrdonnance(@Valid @RequestBody OrdonnanceRequest creq){

		try {
			UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());

			Ordonnance consul=OrdonnanceServices.create(creq, med);

			String fileName = consul.getId_ordon()+"-DOSS"+ consul.getDossier().getId_dossier()+"-MedID"+med.getId();

			//pdfgenerator.generatePdf(consul, fileName, "Patientdoc");
			pdfgenerator.GenartePdfLocaly(consul, fileName, "Patientdoc");

			return ResponseEntity.ok(consul);

		} catch (Exception e) {
			e.printStackTrace();
		return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
		}
		

	}

//update consulation :
@PostMapping("/updateordonnance")
public ResponseEntity<?> Updateordonnance(@Valid @RequestBody OrdonnanceRequest creq){

	try {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();
		
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());

		OrdonnanceDTO consul=OrdonnanceServices.update(creq,med);

		return ResponseEntity.ok(consul);

	} catch (Exception e) {
		e.printStackTrace();
	return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
	}
	

}
// Delete Coonsultation : 
@DeleteMapping(path = "/deleteordonnance/{id}")
public ResponseEntity<?> DeleteOrdonnance(@Valid @PathVariable Long id){
try {
	Ordonnance ordonnance=ordonnanceRepository.findById(id).orElseThrow(()->new Exception("No 	matching found for this ordonnance"));

	Boolean IsDeleted = OrdonnanceServices.deleteById(ordonnance)?true:false;

	return IsDeleted ? ResponseEntity.ok(new ApiResponse(true, "ordonnance has been deleted seccussefully")):ResponseEntity.ok(new ApiResponse(true, "Consultatio cannot be deleted"));

} catch (Exception e) {
	e.printStackTrace();
	return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
}

}

// show data by medecin access right

@GetMapping(path = "/findordonnance/{iddoss}/{idordo}")
public ResponseEntity<?> findOrdoByIdMedecin(@Valid @PathVariable(value = "idordo")Long id,@Valid @PathVariable Long iddoss){

try {
	// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
	// 			.getPrincipal();
	Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
	return ResponseEntity.ok(OrdonnanceServices.findByIdMedandDossierId(med,iddoss,id));
	
} catch (Exception e) {
	e.printStackTrace();
	return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
}



}
@GetMapping(path = "/findordi/{id}")
public ResponseEntity<?> getByIdMedecin(@Valid @PathVariable(value = "id")Long idordo){

	try {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
		Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		OrdonnanceDTO consul=OrdonnanceServices.findById(idordo,med);

		return ResponseEntity.ok(consul);
	} catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
	}
	
	

}

@GetMapping(path = "/allordonnace")
public ResponseEntity<?> findallByIdMedecin(){

try {
	// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
	// 			.getPrincipal();
		
	Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
	List<OrdonnanceDTO> allord=OrdonnanceServices.findAllByMed(med);

	return ResponseEntity.ok(allord);
} catch (Exception e) {
	e.printStackTrace();
	return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));

}


}

//set visibelity 
@PostMapping(path="/setvisibility")
public ResponseEntity<?> SetVisibility()throws Exception{
	UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
	.getPrincipal();

	Medecin med = medecinService.getMedecinByUserId(globalVariables.getConnectedUser().getId());
	return ResponseEntity.ok(medecinService.Myvisibity(med));

}
	//================================================================================
	public static class MedecinParams {

		public Long id;
		public String nom_med;
		public String prenom_med;
		public String photo_med;
		public String diplome_med;
		public String experience_med;
		public String description_med;
		public VilleDTO ville;
		public SpecialiteDTO specialite;
		public UserDTO user;
	}

}
