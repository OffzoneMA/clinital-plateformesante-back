package com.clinital.payload.request;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GetDossierRequest {
    @NotNull
    private Long iddoss;
    private String codeaccess;
    
}
