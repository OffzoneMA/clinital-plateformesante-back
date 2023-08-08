package com.clinital.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import ch.qos.logback.classic.Logger;

public class CustumaccessdeniedHandler implements AccessDeniedHandler {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CustumaccessdeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("Access denied for user " + autentication.getName()+" with roles "+autentication.getAuthorities()+" to "+request.getRequestURI());
        
        
    }
}
    

