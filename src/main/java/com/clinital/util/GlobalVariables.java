package com.clinital.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.clinital.models.User;
import com.clinital.repository.UserRepository;
import com.clinital.security.services.UserDetailsImpl;

import javassist.NotFoundException;

@Component
public class GlobalVariables {

    @Autowired UserRepository userRepository;

    private User user;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
   

    // public User  getConnectedUser() throws Exception {
    //     try{
    //             Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //     if (principal instanceof UserDetailsImpl) {
    //         UserDetailsImpl userDetails = (UserDetailsImpl) principal;
    //         this.user = userRepository.getById(userDetails.getId());
    //         return this.user;
    //     } else {
    //         // Handle the case when the principal is not UserDetailsImpl
    //         // Maybe throw an exception or return null
    //         throw new NotFoundException("Cannot Found a Match");
    //     }
    //     }
    //     catch(Exception e){
    //         System.err.println("Current User Error : "+e.getMessage());
    //         throw new Exception(e.getMessage());
            
    //     }
        
    // }

    public User getConnectedUser() throws NotFoundException {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
            if (principal instanceof UserDetailsImpl) {
                UserDetailsImpl userDetails = (UserDetailsImpl) principal;
                return userRepository.getById(userDetails.getId());
            } else {
                // Handle the case when the principal is not UserDetailsImpl
                throw new NotFoundException("Cannot find a matching user");
            }
        } catch (NotFoundException notFoundException) {
            // Catching and re-throwing custom NotFoundException
            throw notFoundException;
        } catch (Exception e) {
            // Logging the exception using a logging framework (e.g., SLF4J)
            // instead of printing to System.err
            LOGGER.error("Error fetching current user: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching current user");
        }
    }
    

    public void setConnectedUser(User user) {
       this.user=user;
    }
}