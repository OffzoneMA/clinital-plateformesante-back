package com.clinital.models;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.FetchType;

import lombok.Data;

@Entity
@Table(name = "LogActivityUser")
@Data
public class LogActivityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date TimeActivity;
    private String typeActivity;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    
    
    public LogActivityUser(@NotBlank Date TimeActivity, String typeActivity, String description, User user) {
        super();
        this.TimeActivity = TimeActivity;
        this.typeActivity = typeActivity;
        this.description = description;
        this.user = user;
    }



    public LogActivityUser() {
    }
    
}
