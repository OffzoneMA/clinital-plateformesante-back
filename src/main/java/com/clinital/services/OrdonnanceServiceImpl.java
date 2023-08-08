package com.clinital.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.dto.OrdonnanceDTO;
import com.clinital.models.DossierMedical;
import com.clinital.models.Medecin;
import com.clinital.models.Ordonnance;
import com.clinital.models.Rendezvous;
import com.clinital.payload.request.OrdonnanceRequest;
import com.clinital.payload.response.ApiResponse;
import com.clinital.repository.DossierMedicalRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.OrdonnanceRepository;
import com.clinital.repository.PatientRepository;
import com.clinital.repository.RdvRepository;
import com.clinital.services.interfaces.OrdonnanceServices;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.PDFGenerator;


@Transactional
@Service
public class OrdonnanceServiceImpl implements OrdonnanceServices{

    @Autowired
    private DossierMedicalRepository dossierrepo;
    @Autowired
    private MedecinRepository medecinRepository;
    @Autowired
    private RdvRepository rdvRepository;
    @Autowired
    private OrdonnanceRepository ordorepo;
    @Autowired
    private ClinitalModelMapper mapper;
    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;
    

    @Override
    public Ordonnance create(OrdonnanceRequest ordreq, Medecin med) throws Exception {
        try {
            DossierMedical dossier=dossierrepo.findById(ordreq.getDossier()).orElseThrow(()->new Exception("No matching found for this Dossier"));
            Rendezvous rdv=rdvRepository.findById(ordreq.getRendezvous()).orElseThrow(()->new Exception("No matching found for this Rendezvous"));
            Ordonnance ordonnance=new Ordonnance();
            ordonnance.setDate(LocalDate.now());
            ordonnance.setDetails(ordreq.getDetails());
            ordonnance.setMedecin(med);
            ordonnance.setDossier(dossier);
            ordonnance.setRendezvous(rdv);
            ordorepo.save(ordonnance);
            
            return ordonnance;
            
                
            } catch (Exception e) {
            throw new Exception(e.getMessage());
            }    
    }

    @Override
    public OrdonnanceDTO update(OrdonnanceRequest req, Medecin med) throws Exception {
        try {
            Ordonnance ordonnance=ordorepo.findById(req.getId_ordonnance()).orElseThrow(()->new Exception("No matching found for this Ordonnance"));
            DossierMedical dossier=dossierrepo.findById(req.getDossier()).orElseThrow(()->new Exception("No matching found for this Dossier"));
            Rendezvous rdv=rdvRepository.findById(req.getRendezvous()).orElseThrow(()->new Exception("No matching found for this Rendezvous"));
            
            ordonnance.setDate(req.getDate());
            ordonnance.setDetails(req.getDetails());
            ordonnance.setMedecin(med);
            ordonnance.setDossier(dossier);
            ordonnance.setRendezvous(rdv);
            ordorepo.save(ordonnance);
            return mapper.map(ordonnance,OrdonnanceDTO.class);
               
            } catch (Exception e) {
            throw new Exception(e.getMessage());
            }
    
    }
    
    @Override
    public List<OrdonnanceDTO> findAll() throws Exception {
        try {
            List<OrdonnanceDTO> allOrdonnances=ordorepo.findAll().stream().map(consul->mapper.map(consul,OrdonnanceDTO.class)).collect(Collectors.toList());
            return allOrdonnances;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }
    public List<OrdonnanceDTO> findAllByMed(Medecin med) throws Exception {
        try {
            List<OrdonnanceDTO> allOrdonnances=ordorepo.findAllByIdMed(med.getId()).stream().map(consul->mapper.map(consul,OrdonnanceDTO.class)).collect(Collectors.toList());
            return allOrdonnances;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }

    @Override
    public OrdonnanceDTO findById(Long id,Medecin med) throws Exception {
        try {
            Ordonnance ordonnance=ordorepo.findIdandIdMedecin(id,med.getId()).orElseThrow(()->new Exception("No matching found for this Ordonnance"));
            return mapper.map(ordonnance,OrdonnanceDTO.class);  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }

    public ResponseEntity<?> findByIdMedandDossierId(Medecin med,Long iddoss,Long idordo) throws Exception {
        try {
            
		DossierMedical dossier = dossierMedicalRepository.findById(iddoss).orElseThrow(()->new Exception("NO such Folder exist"));


        Ordonnance ordonnance=ordorepo.findIdandIdDossier(idordo, iddoss).orElseThrow(()->new Exception("No Matchng found for this Id ordonnace in this patient folder"));
		// check if the folder is already shared.
		Boolean isDossshared=med.getMeddossiers().stream().filter(doss->doss.getId_dossier()==dossier.getId_dossier()).findFirst().isPresent();

        if(isDossshared){
           return ResponseEntity.ok(ordonnance); 
        }else{

            return ResponseEntity.ok(new ApiResponse(false, "you Cant access to this resource"));
		}

           
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
     
    }

    @Override
    public List<Ordonnance> findByIdName(String name) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean deleteById(Ordonnance ordonnance) throws Exception {
        try {
            
            Boolean IsDeleted= OrdonnanceRepository.deleteOrdonnance(ordonnance.getId_ordon())?true:false;
            return IsDeleted;  
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    

    
}
