package com.clinital.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.dto.MedecinDTO;
import com.clinital.dto.TypeConsultationDTO;
import com.clinital.models.Medecin;
import com.clinital.models.TypeConsultation;
import com.clinital.payload.request.TypeConsultationRequest;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.TypeConsultationRepository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.interfaces.TypeConsultationServices;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;
@Transactional
@Service
public class TypeConsultationServicesImpli implements TypeConsultationServices {

    @Autowired
    private MedecinRepository  Medrepo;
    @Autowired
	private ClinitalModelMapper clinitalModelMapper;
    @Autowired
	TypeConsultationRepository typeConsultationRepository;
    @PersistenceContext
	private EntityManager entityManger;
    @Autowired
    GlobalVariables globalVariables;

    @Override
    public TypeConsultation addTypeConsultation(TypeConsultationRequest TypeConsultation) throws Exception {
        TypeConsultation typeConsult = new TypeConsultation();

        // UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
		// 		.getPrincipal();

        //Medecin Med= Medrepo.getMedecinByUserId();
		Medecin Med= Medrepo.getMedecinByUserId(globalVariables.getConnectedUser().getId());
		//typeConsult = clinitalModelMapper.map(TypeConsultation, TypeConsultation.class);

		typeConsult.setMedecin(Med);
		typeConsult.setTarif(TypeConsultation.getTarif());
		typeConsult.setTitle(TypeConsultation.getTitle());	

		entityManger.merge(typeConsult);
        
        TypeConsultation Consultation = typeConsultationRepository.save(typeConsult);
 

		return clinitalModelMapper.map(typeConsult, TypeConsultation.class);
		 //return ResponseEntity.ok(new ApiResponse(true, "Consultation has been created "));
    }

    @Override
    public TypeConsultationDTO updateTypeConsultation(TypeConsultationRequest TypeConsultation,long id_consultation) throws Exception {
        //TypeConsultation typeConsult = clinitalModelMapper.map(TypeConsultation, TypeConsultation.class);
        
        Optional<TypeConsultation> Consultation = typeConsultationRepository.findById(id_consultation);
        if (Consultation.isPresent()){
            TypeConsultation typeConsult= typeConsultationRepository.getOne(id_consultation);
            typeConsult.setTarif(TypeConsultation.getTarif());
            typeConsult.setTitle(TypeConsultation.getTitle());
            
            TypeConsultation UdpatedConsultaion = typeConsultationRepository.save(typeConsult);
            return clinitalModelMapper.map(UdpatedConsultaion, TypeConsultationDTO.class);
            
        }
        else throw new Exception("Fail to update");
                
            
       
    }

    @Override
    public void deleteTypeConsultation(Long id_consultation , long id_medecin) throws Exception {
        
        
       
            typeConsultationRepository.DeletTypeConsultByMedId(id_medecin, id_consultation);
            
       
        
    }

    @Override
    public List<TypeConsultation> findAllTypeConsultationByMedecinId(Long id_medecin) throws Exception {
       
        return typeConsultationRepository.findAllByMedecinId(id_medecin);
         
    }

    @Override
    public TypeConsultation findTypeConsultationById(Long id_consultation) throws Exception {
         
        return typeConsultationRepository.findTypeConsultationById(id_consultation);
    }
    
}
