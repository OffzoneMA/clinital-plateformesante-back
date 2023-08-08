package com.clinital.controllers;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.enums.CabinetDocStateEnum;
import com.clinital.enums.CabinetDocuemtsEnum;
import com.clinital.models.Cabinet;
import com.clinital.models.DocumentsCabinet;
import com.clinital.models.Medecin;
import com.clinital.payload.request.DocumentsCabinetRequest;
import com.clinital.payload.response.MedecinResponse;
import com.clinital.repository.CabinetRepository;
import com.clinital.repository.DocumentsCabinetRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.services.ActivityServices;
import com.clinital.services.CabinetServiceImpl;
import com.clinital.util.GlobalVariables;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cabinet")
public class CabinetController {


    @Autowired
    CabinetRepository cabinetrep;
    @Autowired
    CabinetServiceImpl cabserve;
	@Autowired
	DocumentsCabinetRepository docCabinetRepository;
	@Autowired
	MedecinRepository medrepo;
	@Autowired
    GlobalVariables globalVariables;
	@Autowired
	private ActivityServices activityServices;

	public final Logger LOGGER=LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/cabinetById/{id}")
	public ResponseEntity<?> getCabinetById(@PathVariable Long id) {
		Optional<Cabinet> cab = cabinetrep.findById(id);

		if (cab.isPresent()) {
            Cabinet cabinet = cabinetrep.getById(id);
			activityServices.createActivity(new Date(), "Read", "Consulting this Anticedent ID "+id, globalVariables.getConnectedUser());
            LOGGER.info("Consulting Cabinet ID : "+id);
			return new ResponseEntity<>(cabinet, HttpStatus.OK);
		} else {
			activityServices.createActivity(new Date(), "Read", "Cannot get this Cabinet ID "+id, globalVariables.getConnectedUser());
                LOGGER.info("Cannot Consulte Cabinet ID : "+id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @GetMapping("/cabinetByName/{name}")
	public ResponseEntity<?> getCabinetByName(@PathVariable String name) throws Exception {
		return ResponseEntity.ok(cabserve.findByIdName(name));
	}

    @GetMapping("/allcabinet")
	public ResponseEntity<?> getAllCabinets() throws Exception {
		return ResponseEntity.ok(cabinetrep.findAll());
	}
	//Admin Role needed to use this :
	@PostMapping("/validation/validerCab/{iddoccab}")
	public ResponseEntity<?> validateCabinetdoc(@Valid @PathVariable long id_doccab) throws Exception{

		DocumentsCabinet doc=docCabinetRepository.findById(id_doccab).orElseThrow(()->new Exception("No matching Found"));
		doc.setValidationState(CabinetDocStateEnum.VALID);
		docCabinetRepository.save(doc);

		Medecin med=medrepo.getMedecinById(doc.getCabinet().getCreator().getId());
		med.setStepsValidation(med.getStepsValidation()+1);
		medrepo.save(med);

		activityServices.createActivity(new Date(), "Update", "Admin Validate Cabinet Documents ", globalVariables.getConnectedUser());
        LOGGER.info("Admin Validate Cabinet Documents");

		return ResponseEntity.ok(doc);

	}
	@PostMapping("/validation/RejectDoccab/{iddoccab}")
	public ResponseEntity<?> RejectCabinetdoc(@Valid @PathVariable long id_doccab) throws Exception{

		DocumentsCabinet doc=docCabinetRepository.findById(id_doccab).orElseThrow(()->new Exception("No matching Found"));
		doc.setValidationState(CabinetDocStateEnum.REJECTED);
		docCabinetRepository.save(doc);
		activityServices.createActivity(new Date(), "Update", "Admin Reject Cabinet Documents ", globalVariables.getConnectedUser());
        LOGGER.info("Admin Reject Cabinet Documents");
		return ResponseEntity.ok(doc);

	}
	@PostMapping("/validation/incompletDoccab/{iddoccab}")
	public ResponseEntity<?> IncompletCabinetdoc(@Valid @PathVariable long id_doccab) throws Exception{

		DocumentsCabinet doc=docCabinetRepository.findById(id_doccab).orElseThrow(()->new Exception("No matching Found"));
		doc.setValidationState(CabinetDocStateEnum.INCOMPLET);
		docCabinetRepository.save(doc);
		activityServices.createActivity(new Date(), "Update", "Admin Update incomplet Cabinet Documents ", globalVariables.getConnectedUser());
        LOGGER.info("Admin Update Incomplet Cabinet Documents");
		return ResponseEntity.ok(doc);

		}
	@GetMapping("/allcabbymedid/{id}")
	public ResponseEntity<?> getAllCabinetByIDMedecin(@Valid @PathVariable(value = "id") long id) throws Exception{
	
	
		List<Cabinet> cabinets=cabserve.allCabinetsByMedID(id);

		activityServices.createActivity(new Date(), "Read", "Admin Consult all Cabinets where work this Medecin with ID "+id, globalVariables.getConnectedUser());
        LOGGER.info("Consult all Cabinets where work this Medecin with ID "+id+" , By Admin : "+globalVariables.getConnectedUser());

		return ResponseEntity.ok(cabinets) ;
	}
	
}
