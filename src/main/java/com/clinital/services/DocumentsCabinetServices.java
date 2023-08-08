package com.clinital.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.dao.IDao;
import com.clinital.models.Cabinet;
import com.clinital.models.DocumentsCabinet;
import com.clinital.payload.request.DocumentsCabinetRequest;
import com.clinital.repository.CabinetRepository;
import com.clinital.repository.DocumentsCabinetRepository;
@Transactional
@Service
public class DocumentsCabinetServices {

    @Autowired
    private CabinetRepository cabrepo;

    @Autowired
    private DocumentsCabinetRepository docrepo;
    public DocumentsCabinet create(DocumentsCabinetRequest docreq) throws Exception {
        Cabinet cabinet = cabrepo.findById(docreq.getId_cabinet()).orElseThrow(()->new Exception("No Matching Cabinet found"));
			
			DocumentsCabinet documentEntity = new DocumentsCabinet();
			documentEntity.setType_doc(docreq.getType());
			documentEntity.setDate_ajout_doc(LocalDate.now());
			documentEntity.setCabinet(cabinet);
            docrepo.save(documentEntity);
        return documentEntity;
    }

    
    public void update(DocumentsCabinetRequest o) {
        // TODO Auto-generated method stub
        //to do it soon
        // to doit it 
        
    }

    
    public void delete(Long id) {
        // TODO Auto-generated method stub
        
    }

    
    public List<DocumentsCabinet> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Optional<DocumentsCabinetRequest> findById(long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }
    
}
