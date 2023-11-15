package com.clinital.payload.response;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SharingHistoryResponse {
   long id;
    @NotBlank
    long user;
    @NotBlank
    long patient;
    @NotBlank
    long medecin;
    @NotBlank
    long dossier;
    @NotBlank
    LocalDateTime sharingdate; 
}
