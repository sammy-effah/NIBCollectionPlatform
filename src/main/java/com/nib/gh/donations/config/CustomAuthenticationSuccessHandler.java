package com.nib.gh.donations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.nib.gh.donations.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

        if (!userRepository.existsByUsernameAndEnabled(authentication.getName().split("@")[0], true)) {
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            // hasInstitutionRole
            if (userRepository.hasAdminRole(authentication.getName().split("@")[0])) {
                response.sendRedirect(request.getContextPath() + "/admin");
            } else if (userRepository.hasInstitutionRole(authentication.getName().split("@")[0])) {
                response.sendRedirect(request.getContextPath() + "/institutions");
            } else {
                response.sendRedirect(request.getContextPath() + "/donatee");
            }
        }

    }
}
