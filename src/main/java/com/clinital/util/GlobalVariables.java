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

   

    public User  getConnectedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            this.user = userRepository.getById(userDetails.getId());
            return this.user;
        } else {
            // Handle the case when the principal is not UserDetailsImpl
            // Maybe throw an exception or return null
            return null;
        }
    }

    public void setConnectedUser(User user) {
       this.user=user;
    }
}