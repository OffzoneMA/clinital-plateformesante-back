package com.clinital.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ActivityDTO {

    private Long id;
    @NotNull
    private Date TimeActivity;
    @NotNull
    private String typeActivity;
    @NotNull
    private String description;
    @NotNull
    private UserDTO user;



    
}
