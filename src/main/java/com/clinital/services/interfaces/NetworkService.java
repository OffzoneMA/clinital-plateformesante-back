package com.clinital.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.clinital.dto.MedecinDTO;
import com.clinital.dto.MedecinNetworkDTO;
import com.clinital.models.Medecin;
import com.clinital.models.MedecinNetwork;
import com.clinital.payload.request.networkRequest;

public interface NetworkService {

    public MedecinNetwork addMedecinNetwork(networkRequest medecinNetwor,long id) throws Exception;
    
    public MedecinNetworkDTO updateMedecinNetwork(MedecinNetworkDTO medecinNetworkDTO) throws Exception;
    
    public void deleteMedecinNetwork(Long id_medecin, Long id_follower) throws Exception;
    
    public List<?> getAllMedecinNetwork(Long id_medecin) throws Exception;
    
    public Medecin getMedecinfollewerById(Long id_medecin, Long id_follower) throws Exception;

    public MedecinNetwork FindMedecinsNetworkByID(Long id_medecin, Long id_follower) throws Exception;
    
   
    
  
    
}
