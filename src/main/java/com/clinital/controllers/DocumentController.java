package com.clinital.controllers;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.clinital.dto.DocumentDTO;
import com.clinital.dto.PatientDTO;
import com.clinital.models.Document;
import com.clinital.models.Patient;
import com.clinital.models.Rendezvous;
import com.clinital.models.User;
import com.clinital.payload.request.DocumentRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.payload.response.DocumentResponse;
import com.clinital.payload.response.TypeDocumentResponse;
import com.clinital.repository.DocumentRepository;
import com.clinital.repository.PatientRepository;
import com.clinital.repository.RdvRepository;
import com.clinital.repository.TypeDocumentRepository;
import com.clinital.repository.UserRepository;
import com.clinital.security.config.azure.AzureServices;
import com.clinital.security.jwt.JwtUtils;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.ActivityServices;
import com.clinital.services.DocumentPatientServices;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.SharedAccessAccountPolicy;
import com.microsoft.azure.storage.StorageException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/doc")
public class DocumentController {

	@Autowired
	DocumentRepository docrepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Value(value = "${azure.storage.account-key}")
	String azureStorageToken;

	@Autowired
   	 private AzureServices azureAdapter;


	@Autowired
	ClinitalModelMapper mapper;

	@Autowired
	PatientRepository patientRepo;

	@Autowired
	TypeDocumentRepository typeDocumentRepo;

	@Autowired
	RdvRepository rdvRepository;

	@Autowired
	DocumentPatientServices docservices;

	@Autowired
    GlobalVariables globalVariables;

	@Autowired
	private ActivityServices activityServices;

	private final Logger LOGGER=LoggerFactory.getLogger(getClass());

	// Returning a list of documents. %OK%
	@GetMapping("/documents")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	Iterable<DocumentResponse> documents() {
		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();

		List<Patient> allPatients = patientRepo.findAllById(globalVariables.getConnectedUser().getId());
		activityServices.createActivity(new Date(),"Read","Consulting all Documents",globalVariables.getConnectedUser());
		LOGGER.info("Conslting All documents By User ID : "+globalVariables.getConnectedUser().getId());
		return docrepository
				.findByPatientIdIn(allPatients.stream().map(patient -> patient.getId()).collect(Collectors.toList()))
				.stream().map(document -> mapper.map(document, DocumentResponse.class)).collect(Collectors.toList());

	}

	// Returning a document by id. %OK%
	@GetMapping("/docById/{id}")
	public ResponseEntity<DocumentResponse> getDocById(@PathVariable long id) {
		Optional<Document> tutorialData = docrepository.findById(id);

		if (tutorialData.isPresent()) {
			activityServices.createActivity(new Date(),"Read","Consulting  Document ID : "+id,globalVariables.getConnectedUser());
			LOGGER.info("Conslting  document ID "+id+" ,By User ID : "+globalVariables.getConnectedUser().getId());
			return new ResponseEntity<>(mapper.map(tutorialData.get(), DocumentResponse.class), HttpStatus.OK);
		} else {
			
			LOGGER.warn("Cant Found Doc ID : "+id+",Consulting By User ID : "+globalVariables.getConnectedUser().getId());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Returning a list of documents by patient id. %OK%
	@PostMapping("/docByIdPatient")
	@ResponseBody
	public List<DocumentResponse> findDocByIdPatient(@RequestParam("patient") Long patientId) {
		activityServices.createActivity(new Date(),"Read","Conslting  documents Pateint  ID :"+patientId,globalVariables.getConnectedUser());
		LOGGER.info("Conslting  documents Pateint  ID :"+patientId+" By User ID : "+globalVariables.getConnectedUser().getId());
		return docrepository.getDocByIdPatient(patientId).stream().map(doc -> mapper.map(doc, DocumentResponse.class))
				.collect(Collectors.toList());
	}

	@PostMapping("/docByIdRdv")
	@ResponseBody
	public List<DocumentResponse> findDocByIdRdv(@RequestParam Long rdvId) {
		activityServices.createActivity(new Date(),"Read","Conslting  documents By Rdv  ID :"+rdvId,globalVariables.getConnectedUser());
		LOGGER.info("Conslting  documents by RDV ID :"+rdvId+" By User ID : "+globalVariables.getConnectedUser().getId());
		return docrepository.getDocByIdRendezvous(rdvId).stream().map(doc -> mapper.map(doc, DocumentResponse.class))
				.collect(Collectors.toList());
	}

	@GetMapping("/docByNomPatient")
	@ResponseBody
	public List<DocumentResponse> findDocByNomPatient(@RequestParam String nomPatient) {
		activityServices.createActivity(new Date(),"Read","Conslting  documents Pateint  name :"+nomPatient,globalVariables.getConnectedUser());
		LOGGER.info("Conslting  documents Pateint  name :"+nomPatient+" By User ID : "+globalVariables.getConnectedUser().getId());
		return docrepository.getDocByNomPatient(nomPatient).stream().map(doc -> mapper.map(doc, DocumentResponse.class))
				.collect(Collectors.toList());
	}
// add a document of a patient :
	@PostMapping(path = "/addDoc")
	@ResponseBody
	public ResponseEntity<?> addDoc(@RequestParam String document,
			@RequestParam MultipartFile docFile) throws Exception {
				
				try {
					//----Add document : 
			
			// ------ Save Doc 
			Document savedDoc = docservices.create(document);
			// --------------------- Initial doc Upload :
			String extension = FilenameUtils.getExtension(docFile.getOriginalFilename());
			String fileName = savedDoc.getTitre_doc()+ "." + extension;
			String UploadedFile = azureAdapter.upload(docFile,fileName,"Patientdoc");
			// --------------- retirve file nam e after Upload
			savedDoc.setFichier_doc(UploadedFile);

			savedDoc.setNumero_doc(savedDoc.getId_doc());
			
			Document finalSavedDoc = docrepository.save(savedDoc);
			// ----- add doc info to RDV :
			Rendezvous rdv= rdvRepository.findById(finalSavedDoc.getRendezvous().getId()).orElseThrow(()->new Exception("NO MATCHING FOUND"));
			rdv.getDocuments().add(finalSavedDoc);
			rdvRepository.save(rdv);
			activityServices.createActivity(new Date(),"Add","Add New document ID:"+finalSavedDoc.getId_doc(),globalVariables.getConnectedUser());
			LOGGER.info("Add new  documents ID :"+finalSavedDoc.getId_doc()+" By User ID : "+globalVariables.getConnectedUser().getId());
			return ResponseEntity.ok(new ApiResponse(true, "Document created successfully!",finalSavedDoc));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse(false, "Document not created!"+e.getMessage()));

		}

	}
	// Download a file of a patient : 
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
		  activityServices.createActivity(new Date(),"Read","Download document ID:",globalVariables.getConnectedUser());
		  LOGGER.info("Dowanload  document ID :... By User ID : "+globalVariables.getConnectedUser().getId());
      return ResponseEntity.ok().contentType(MediaType
                  .APPLICATION_OCTET_STREAM)
            .headers(headers).body(resource);
   }



// Returning a list of documents by patient id. %OK%

	@GetMapping("/allDocByPatientId/{id}")
	@ResponseBody
	public List<DocumentResponse> findAllDocsByPatientId(@PathVariable("id") long id ) throws Exception {

		
		Patient patient=patientRepo.findById(id).orElseThrow(()->new Exception("No Match Found"));

		List<Document> documents = docrepository.findByPatientId(patient.getId());
		activityServices.createActivity(new Date(),"Read","Consulting All  documents by patient ID :"+id,globalVariables.getConnectedUser());
		LOGGER.info("Consulting All  documents by patient ID :"+id+" By User ID : "+globalVariables.getConnectedUser().getId());
		return documents.stream().map(doc -> mapper.map(doc, DocumentResponse.class)).collect(Collectors.toList());
	}

	// Returning a list of archived documents by patient id. %OK%
	@GetMapping("/allArchivedDocByPatientId/{id}")
	@ResponseBody
	public List<DocumentDTO> findAllArchivedDocsByPatientId(@PathVariable("id") long id) throws Exception {

				
		Patient patient=patientRepo.findById(id).orElseThrow(()->new Exception("No Match Found"));

		List<Document> documents = docrepository.findByPatientIdAndArchived(patient.getId(), true);
		activityServices.createActivity(new Date(),"Read","Consulting Archived All  documents by patient ID :"+id,globalVariables.getConnectedUser());
		LOGGER.info("Consulting All Archived  documents by patient ID :"+id+" By User ID : "+globalVariables.getConnectedUser().getId());
		return documents.stream().map(doc -> mapper.map(doc, DocumentDTO.class)).collect(Collectors.toList());
	}

	// Returning a list of type documents. %OK%
	@GetMapping("/typeDocuments")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	Iterable<TypeDocumentResponse> getTypeDocuments() {
		activityServices.createActivity(new Date(),"Read","Consulting All Type  documents",globalVariables.getConnectedUser());
		LOGGER.info("Consulting All Types  documents By User ID : "+globalVariables.getConnectedUser().getId());
		return typeDocumentRepo.findAll().stream().map(typeDoc -> mapper.map(typeDoc, TypeDocumentResponse.class))
				.collect(Collectors.toList());

	}

	// Generating a SAS token for the Azure Blob Storage. %OK%
	@GetMapping("/getSasToken")
	public ResponseEntity<?> getSAS() throws InvalidKeyException, URISyntaxException, StorageException {

		String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=documentspatient;AccountKey="
				+ azureStorageToken + ";EndpointSuffix=core.windows.net";
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

		SharedAccessAccountPolicy sharedAccessAccountPolicy = new SharedAccessAccountPolicy();
		sharedAccessAccountPolicy.setPermissionsFromString("racwdlup");
		long date = new Date().getTime();
		long expiryDate = new Date(date + 8640000).getTime();
		sharedAccessAccountPolicy.setSharedAccessStartTime(new Date(date));
		sharedAccessAccountPolicy.setSharedAccessExpiryTime(new Date(expiryDate));
		sharedAccessAccountPolicy.setResourceTypeFromString("sco");
		sharedAccessAccountPolicy.setServiceFromString("bfqt");
		String sasToken = storageAccount.generateSharedAccessSignature(sharedAccessAccountPolicy);

		return ResponseEntity.ok(new ApiResponse(true, sasToken));
	}

	
	
	// A method that returns a list of documents by patient id and medecin %OK% 
	@GetMapping("/getDocByPatientIdAndMedecin")
	@ResponseBody
	public List<DocumentResponse> getDocByPatientIdAndMedecin() {

		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();

		List<Patient> allPatients = patientRepo.findAllById(globalVariables.getConnectedUser().getId());
		List<Document> documents = docrepository.getDocByPatientIdAndMedecin(
				allPatients.stream().map(patient -> patient.getId()).collect(Collectors.toList()));
				activityServices.createActivity(new Date(),"Read","Consulting All  documents",globalVariables.getConnectedUser());
				LOGGER.info("Consulting All Archived  documents by patient ID ,User ID : "+globalVariables.getConnectedUser().getId());
		return documents.stream().map(doc -> mapper.map(doc, DocumentResponse.class)).collect(Collectors.toList());
	}


	@GetMapping("/getdocproch/{idpat}")
	@ResponseBody
	public List<DocumentResponse> getDocProchPatientId(@Valid @PathVariable("idpat")long patid) throws Exception {

		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();

		List<Patient> allPatients = patientRepo.findAllById(globalVariables.getConnectedUser().getId());
		List<Document> documents = docrepository.getDocPatientPROCH(
				allPatients.stream().map(patient -> patient.getId()).collect(Collectors.toList()));
		if(!documents.isEmpty()){
			activityServices.createActivity(new Date(),"Read","Consulting All Proch patient  documents by patient",globalVariables.getConnectedUser());
		LOGGER.info("Consulting All Proch patient  documents by patient ID : "+globalVariables.getConnectedUser().getId());
			return documents.stream().map(doc -> mapper.map(doc, DocumentResponse.class)).collect(Collectors.toList());
		}else throw new Exception(" NO MATCHING FOUND !");
		
	}


	@GetMapping("/getdocmoi/{idpat}")
	@ResponseBody
	public List<DocumentResponse> getDocMoiPatientId(@Valid @PathVariable("idpat")long patid) throws Exception {

		// UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();

		List<Patient> allPatients = patientRepo.findAllById(globalVariables.getConnectedUser().getId());
		List<Document> documents = docrepository.getDocPatientMOI(
				allPatients.stream().map(patient -> patient.getId()).collect(Collectors.toList()));
		if(!documents.isEmpty()){
			activityServices.createActivity(new Date(),"Read","Consulting All documents for Connected user ",globalVariables.getConnectedUser());
		LOGGER.info("Consulting All documents for connected User ID : "+globalVariables.getConnectedUser().getId());
			return documents.stream().map(doc -> mapper.map(doc, DocumentResponse.class)).collect(Collectors.toList());
		}else throw new Exception(" NO MATCHING FOUND !");
		
	}

	// A  request to the archive a doc endpoint. %ok%
	@PostMapping(path = "/archiveDoc")
	public ResponseEntity<?> archiveDoc(@RequestParam Long docId, @RequestParam boolean archive)
			throws Exception {
		Optional<Document> document = docrepository.findById(docId);
		if(document!=null && document.get().getArchived()==true){
			document.get().setArchived(archive);

		docrepository.save(document.get());
		activityServices.createActivity(new Date(),"Update","Archive document ID :"+docId,globalVariables.getConnectedUser());
		LOGGER.info("Archive document ID :"+docId+"by User ID : "+globalVariables.getConnectedUser().getId());
		return ResponseEntity.ok(new ApiResponse(true, "Archived"));
		} else 
		
		LOGGER.info("Archive document failed by User ID : "+globalVariables.getConnectedUser().getId());
		return ResponseEntity.ok(new ApiResponse(false, "no Matchng found !"));

		

	}

	// A method that is called when a user  delete a Doc. %OK%
	@PostMapping(path = "/deleteDoc")
	public ResponseEntity<?> deleteDoc(@RequestParam Long docId) throws Exception {
		Optional<Document> document = docrepository.findById(docId);

		docrepository.delete(document.get());
		azureAdapter.deleteBlob(document.get().getFichier_doc());
		ResponseEntity.status(200).build();
		activityServices.createActivity(new Date(),"Delete","Delete document ID :"+docId,globalVariables.getConnectedUser());
		LOGGER.info("Delete document ID :"+docId+"by User ID : "+globalVariables.getConnectedUser().getId());
		return ResponseEntity.ok(new ApiResponse(true, "File has been Deleted"));

	}

	public static class DocumentParams {

		public Long id_doc;
		public Long numero_doc;
		public String type_doc;
		public String titre_doc;
		public Date date_ajout_doc;
		public String auteur;
		public String fichier_doc;
		public PatientDTO patient;
	}

}
