package com.nib.gh.donations.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@EnableWebMvc
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    Logger logger = LogManager.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("AUTHENTICATION:: " + auth);
        logger.info("CONTEXT PATH:: " + httpServletRequest.getContextPath());
        if (httpServletRequest.getMethod().equalsIgnoreCase("POST") && httpServletRequest.getRequestURI().contains("/nib/api/")) {
            // Allow POST request to /login to proceed
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (auth == null || !auth.isAuthenticated()) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
        }
    }
}

