package com.clinital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class ExperienceMedecinDTO {

    @NotBlank
    private String nom_experience;

    @NotBlank
    private Date date_debut;

    @NotBlank
    private Date date_fin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MedecinDTO medecinDTO;
}
