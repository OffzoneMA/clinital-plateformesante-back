package com.clinital.services;

import java.util.List;

import javax.transaction.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.dto.CabinetMedecinDTO;
import com.clinital.models.Cabinet;
import com.clinital.models.CabinetMedecinsSpace;
import com.clinital.models.Medecin;
import com.clinital.payload.request.CabinetMedecinsSpaceRequest;
import com.clinital.repository.CabinetMedecinRepository;
import com.clinital.repository.CabinetRepository;
import com.clinital.services.interfaces.MedecinCabinetService;

@Service
@Transactional
public class CabinetMedecinServiceImpl implements MedecinCabinetService {
    @Autowired
    private MedecinServiceImpl medservice;
    @Autowired
    private CabinetRepository cabrep;
    @Autowired
    private CabinetMedecinRepository cabmedrepo;

    @Override
    public CabinetMedecinsSpace addCabinetMedecinsSpace(CabinetMedecinsSpaceRequest medecincabinetreq, Cabinet cabinet,
            Medecin medecin) throws Exception {
        
        
        CabinetMedecinsSpace CabMed=new CabinetMedecinsSpace(medecin,cabinet,medecincabinetreq.getStatus());
        cabmedrepo.save(CabMed);

        return CabMed;
    }

    @Override
    public CabinetMedecinsSpace updateCabinetMedecinsSpace(CabinetMedecinDTO CabinetMedecinDTO) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

   
    public void deleteCabinetMedecins(Long idcab) throws Exception {
        cabmedrepo.DeleteCabinetbyID(idcab);
        
    }

    @Override
    public List<?> getAllCabinetMedecinsSpacebyIdMed(Long id_medecin) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CabinetMedecinsSpace getCabinetMedecinsSpaceByIdMedandIdCab(Long id_medecin, Long id_cabinet)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CabinetMedecinsSpace FindMedecinsNetworkByID(Long id_medecin, Long id_follower) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteCabinetMedecinsSpace(Long id_cabinet) throws Exception {
        // TODO Auto-generated method stub
        
    }

   
    
}
