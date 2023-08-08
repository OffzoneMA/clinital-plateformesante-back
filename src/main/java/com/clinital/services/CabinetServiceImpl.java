package com.clinital.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.dto.CabinetDTO;
import com.clinital.models.Cabinet;
import com.clinital.models.Medecin;
import com.clinital.models.Ville;
import com.clinital.payload.request.CabinetRequest;
import com.clinital.repository.CabinetMedecinRepository;
import com.clinital.repository.CabinetRepository;
import com.clinital.repository.MedecinRepository;
import com.clinital.repository.VilleRepository;
import com.clinital.security.services.UserDetailsImpl;
import com.clinital.services.interfaces.CabinetService;
import com.clinital.util.ClinitalModelMapper;
@Transactional
@Service
public class CabinetServiceImpl implements CabinetService{
	
	
	@Autowired
	private CabinetRepository cabinetRepository;

	@Autowired
	private MedecinRepository medrepo;

	@Autowired
	private ClinitalModelMapper modelMapper;

	@Autowired
	private CabinetMedecinRepository cabmedrepo;

	@Autowired
	private VilleRepository villerepo;

	@Override
	public Cabinet create(CabinetRequest cabinetreq,Medecin med) throws Exception {
		
		Ville ville= villerepo.findById(cabinetreq.getId_ville()).orElseThrow(()->new Exception("No matching ville"));
		Cabinet cabinet = new Cabinet();
		cabinet.setNom(cabinetreq.getNom());
		cabinet.setAdresse(cabinetreq.getAdresse());
		cabinet.setCode_post(cabinetreq.getCode_post());
		cabinet.setVille(ville);
		cabinet.setPhoneNumber(cabinetreq.getPhoneNumber());
		cabinet.setCreator(med);
		cabinet.setState(false);
		cabinetRepository.save(cabinet);
	
		return cabinet;
	}

	@Override
	public CabinetDTO update(CabinetDTO dto, Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CabinetDTO> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CabinetDTO findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteById(Long idcab) throws Exception {
		
		cabinetRepository.deleteById(idcab);
		//categoryRepository.deleteAll();
    	
		 
		
	}

	@Override
	public List<Cabinet> findByIdName(String name) throws Exception {
		List<Cabinet> cabinets=cabinetRepository.findByNomContainingIgnoreCase(name)
		.stream().map(cabinet->modelMapper.map(cabinet, Cabinet.class) )
		.collect(Collectors.toList());
		return cabinets;
	}

	// Setting the Validation State  of the Cabinet.
	public Cabinet CabinetStateValidation(long id) throws Exception {
			try {
				Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(()->new Exception("No Matching Found for this ID Cabinet"));
					
				
					cabinetRepository.setCabinetSTate(!cabinet.getState(),cabinet.getId_cabinet());
					return cabinet;
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		
		
	}

	@Override
	public List<Cabinet> allCabinetsByMedID(Long id) throws Exception {
		// TODO Auto-generated method stub
  try {
	Medecin med=medrepo.findById(id).orElseThrow(()->new Exception("No Matching Medecin for this ID"));
		List<Cabinet> cabinets=cabinetRepository.getAllCabinetByIdMed(med.getId()).stream().map(cab->modelMapper.map(cab, Cabinet.class)).collect(Collectors.toList());
		
		return  cabinets;
  } catch (Exception e) {
	// TODO: handle exception
	throw new Exception(e.getMessage());
  }
		
		
	}

}
