package com.clinital.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.clinital.models.User;
import com.clinital.repository.UserRepository;
import com.clinital.security.services.UserDetailsImpl;

@Component
public class GlobalVariables {

    @Autowired UserRepository userRepository;

    private User user;

   

    public User getConnectedUser() {
        return user;
    }

    public void setConnectedUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        this.user = userRepository.getById(userDetails.getId());
    }
}