package com.clinital.security.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.clinital.enums.ERole;
import com.clinital.models.User;
import com.clinital.repository.UserRepository;

@Service
public class CustomOAuth2UserService2 implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to default implementation
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // Extract necessary information
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Check if the user exists in the database, if not, create and save the user
        User user = userRepository.findUserByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setRole(ERole.ROLE_PATIENT);
                    newUser.setEnabled(true);
                    newUser.setEmailVerified(true);
                    // Set other profile information
                    return userRepository.save(newUser);
                });

        // Return user information
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_PATIENT")),
                oauth2User.getAttributes(),
                "email");
    }
}