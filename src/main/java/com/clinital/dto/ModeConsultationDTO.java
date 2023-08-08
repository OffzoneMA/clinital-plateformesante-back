package com.clinital.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ModeConsultationDTO {
    @NotBlank
    private long id;
    @NotBlank
    private String mode;

public ModeConsultationDTO(){
    super();
}

}
