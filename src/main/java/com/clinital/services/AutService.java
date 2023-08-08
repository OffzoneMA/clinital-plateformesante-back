package com.clinital.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.models.User;
import com.clinital.payload.request.SignupRequest;
import com.clinital.repository.ConfirmationTokenRepository;
import com.clinital.repository.UserRepository;
import com.clinital.security.ConfirmationToken;
import com.clinital.security.jwt.JwtUtils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Transactional
@Service
public class AutService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
	PasswordEncoder encoder;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);        
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User saveUser(SignupRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setTelephone(signUpRequest.getTelephone());
        user.setPassword(signUpRequest.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(user.getRole());
        return userRepository.save(user);
    }
    
    public ConfirmationToken createToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        return confirmationTokenRepository.save(confirmationToken);
    }
    public ConfirmationToken findByConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }
    public void deleteToken(ConfirmationToken confirmationToken) {
        this.confirmationTokenRepository.delete(confirmationToken);
    }
}