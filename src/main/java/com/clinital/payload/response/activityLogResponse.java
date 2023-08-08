package com.clinital.payload.response;

import java.util.Date;

import com.clinital.dto.UserDTO;

import lombok.Data;

@Data
public class activityLogResponse {
        
        private Long id;
        private Date TimeActivity;
        private String typeActivity;
        private String description;
        private UserDTO user;
        
    
}
