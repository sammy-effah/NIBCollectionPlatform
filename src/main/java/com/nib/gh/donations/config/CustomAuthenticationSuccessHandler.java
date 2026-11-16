package com.nib.gh.donations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.nib.gh.donations.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();

        if (userRepository.hasAdminRole(username)) {
            response.sendRedirect(request.getContextPath() + "/admin");
        } else if (userRepository.hasInstitutionRole(username)) {
            response.sendRedirect(request.getContextPath() + "/institutions");
        } else {
            response.sendRedirect(request.getContextPath() + "/donatee");
        }
    }
}
