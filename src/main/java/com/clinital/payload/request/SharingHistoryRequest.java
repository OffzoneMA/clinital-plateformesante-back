package com.clinital.payload.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SharingHistoryRequest {

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
