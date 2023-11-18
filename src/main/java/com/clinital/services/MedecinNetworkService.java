package com.clinital.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.storage.internal.avro.implementation.schema.primitive.AvroNullSchema.Null;
import com.clinital.dto.MedecinDTO;
import com.clinital.dto.MedecinNetworkDTO;
import com.clinital.exception.BadRequestException;
import com.clinital.models.Medecin;
import com.clinital.models.MedecinFollowersID;
import com.clinital.models.MedecinNetwork;
import com.clinital.payload.request.networkRequest;
import com.clinital.repository.MedecinNetworkRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.interfaces.NetworkService;
import com.clinital.util.ClinitalModelMapper;
import com.clinital.util.GlobalVariables;

@Transactional
@Service
public class MedecinNetworkService implements NetworkService {

    @Autowired
    private MedecinNetworkRepository medecinNetworkRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
	private ClinitalModelMapper clinitalModelMapper;
    
    @Autowired
    private ActivityServices activityServices;

    @Autowired
    private GlobalVariables globalVariables;

    private final Logger LOGGER=LoggerFactory.getLogger(getClass());
    public MedecinNetworkService() {
        super();
    }
    
    /**
     * It adds a MedecinNetworkDTO to the database.
     * 
     * @param medecinNetworkDTO 
     * @return Nothing.
     */
    public MedecinNetwork addMedecinNetwork(networkRequest medecinNetwork,long user_id) {

       			
        Medecin med = medecinRepository.getMedecinByUserId(user_id);

        Medecin follower = medecinRepository.getMedecinById(medecinNetwork.getFollower_id());

  

        if(med.getId()!= follower.getId()){

            MedecinNetwork  Medecinnetwork = new MedecinNetwork(med,follower,medecinNetwork.getComment());
        
             MedecinNetwork MedNet = medecinNetworkRepository.save(Medecinnetwork);

             return MedNet;
            

        }
        else
			throw new BadRequestException("Not allowed"); 

        

    }
    
    
    /**
     * It updates the medecin network.
     * 
     * @param medecinNetworkDTO 
     * @return Nothing.
     */
    public MedecinNetworkDTO updateMedecinNetwork(MedecinNetworkDTO medecinNetworkDTO) {
        return null;
    }
    
    /**
     * Delete a medecin network
     * 
     * @param id_medecin the id of the doctor
     * @param id_follower the id of the user who wants to follow the doctor
     * @return A MedecinNetworkDTO object.
     * @throws Exception
     */
    public void deleteMedecinNetwork(Long id_medecin, Long id_follower) throws Exception {
        
        MedecinNetwork follower = medecinNetworkRepository.FindMedecinsNetworkByID(id_medecin, id_follower);
        
        if(follower!=null){
            activityServices.createActivity(new Date(),"Delete","Delete Medecin from Network By ID : "+id_follower+"  for Connected Medecin Network",globalVariables.getConnectedUser());
            LOGGER.info("Delete Medecin follwer from Network by id "+id_follower+" for Medecin Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
           medecinNetworkRepository.deleteNetworkById(follower.getMedecin().getId(), follower.getFollower().getId());

        } else
        activityServices.createActivity(new Date(),"error","Failed to Delete Medecin from Network By ID : "+id_follower+"  for Connected Medecin Network",globalVariables.getConnectedUser());
        LOGGER.error("Failed Delete Medecin follwer from Network by id "+id_follower+" for Medecin Connected, User ID  : "+(globalVariables.getConnectedUser() instanceof User ? globalVariables.getConnectedUser().getId():""));
        throw new Exception("Fail to delete");
    }
    
   /**
    * It returns a MedecinNetworkDTO object.
    * 
    * @param id_medecin the id of the doctor
    * @return A MedecinNetworkDTO object.
 * @throws Exception
    */
    public List<Medecin> getAllMedecinNetwork(Long id_medecin) throws Exception {


        return medecinRepository.getAllMedecinNetwork(id_medecin);
    }
    
   /**
    * It returns a MedecinNetworkDTO object.
    * 
    * @param id_medecin the id of the doctor
    * @param id_follower the id of the user who wants to follow the doctor
    * @return Nothing.
    */
@Override
public Medecin getMedecinfollewerById(Long id_medecin, Long id_follower) throws Exception {

    return medecinRepository.getMedecinsFollowerByID(id_medecin, id_follower);

    // TODO Auto-generated method stub

}

@Override
public MedecinNetwork FindMedecinsNetworkByID(Long id_medecin, Long id_follower) throws Exception {
    
    return  medecinNetworkRepository.FindMedecinsNetworkByID(id_medecin, id_follower);
}
    
    
    
}
