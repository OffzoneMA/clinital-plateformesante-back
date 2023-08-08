package com.clinital.payload.request;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.clinital.enums.CiviliteEnum;
import com.clinital.models.CabinetMedecinsSpace;
import com.clinital.models.DiplomeMedecin;
import com.clinital.models.Document;
import com.clinital.models.DossierMedical;
import com.clinital.models.ExperienceMedecin;
import com.clinital.models.ExpertisesMedecin;
import com.clinital.models.MoyenPaiement;
import com.clinital.models.Specialite;
import com.clinital.models.User;
import com.clinital.models.Ville;

import lombok.Data;

@Data
public class MedecinRequest {

    private Long id;
	private String matricule_med;
	private String inpe;
	private String nom_med;
	private String prenom_med;
	private String photo_med;
	private String photo_couverture_med;
	private Long diplome_med;
	private String description_med;
	private String contact_urgence_med;
	@Enumerated(value = EnumType.STRING)
	private CiviliteEnum civilite_med;
	private Long ville;
	private Long specialite;
	private Long user;
	private Long cabinet;
	
	

	

 
    
}
