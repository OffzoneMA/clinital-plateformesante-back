package com.clinital.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.dto.SpecialiteDTO;
import com.clinital.repository.SpecialiteRepository;
import com.clinital.services.interfaces.SpecialiteService;
import com.clinital.util.ClinitalModelMapper;

@Transactional
@Service
public class SpecialiteServiceImpl implements SpecialiteService{
	
	@Autowired
	private SpecialiteRepository specialiteRepository;
	
	@Autowired
	private ClinitalModelMapper modelMapper;

	@Override
	public List<SpecialiteDTO> findAll() {
		return specialiteRepository.findAll()
				.stream()
				.map(sp->modelMapper.map(sp, SpecialiteDTO.class))
				.collect(Collectors.toList());
	}

}
