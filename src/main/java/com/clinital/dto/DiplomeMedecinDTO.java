package com.clinital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class DiplomeMedecinDTO {

    @NotBlank
    private String nom_diplome;

    @NotBlank
    private Date date_debut;

    @NotBlank
    private Date date_fin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MedecinDTO medecinDTO;
}
