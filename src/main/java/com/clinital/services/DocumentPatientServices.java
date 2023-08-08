package com.clinital.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.models.Document;
import com.clinital.models.Patient;
import com.clinital.models.Rendezvous;
import com.clinital.models.TypeDocument;
import com.clinital.payload.request.DocumentRequest;
import com.clinital.repository.DocumentRepository;
import com.clinital.repository.PatientRepository;
import com.clinital.repository.RdvRepository;
import com.clinital.repository.TypeDocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class DocumentPatientServices {

    @Autowired
    private DocumentRepository docrepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private RdvRepository rdvRepository;

    @Autowired
    private TypeDocumentRepository typeDocumentRepo;

    public Document create(String document) throws Exception{
        try {
            
            
            ObjectMapper om = new ObjectMapper();
			
            DocumentRequest documentReq = om.readValue(document, DocumentRequest.class);
            Patient patient = patientRepo.findById(documentReq.getPatientId()).orElseThrow(()->new Exception("No Matching Patient found"));
            Rendezvous rendezvous =  rdvRepository.findById(documentReq.getRdvId()).orElseThrow(()->new Exception("No Matching RDV found"));
            TypeDocument typedoc= typeDocumentRepo.findById(documentReq.getTypeDocId()).orElseThrow(()->new Exception("No Matching type doc found"));
            Document documentEntity = new Document();
            documentEntity.setTitre_doc(documentReq.getTitre_doc());
            documentEntity.setTypeDoc(typedoc);
            documentEntity.setPatient(patient);
            documentEntity.setDossier(patient.getDossierMedical());
            documentEntity.setArchived(false);
            documentEntity.setDate_ajout_doc(new Date());
            documentEntity.setRendezvous(rendezvous);
            docrepo.save(documentEntity);
            return documentEntity;
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception(e.getMessage());
        }
    }
}
