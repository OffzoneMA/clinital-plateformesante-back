package com.clinital.payload.response;

import java.util.Date;

import lombok.Data;

@Data
public class CabinetResponse {
	
	private Long id_cabinet;
	private String nom;
	private String adresse;
	private String code_post;
	private Date horaires;
	private String phoneNumber;
	

}
