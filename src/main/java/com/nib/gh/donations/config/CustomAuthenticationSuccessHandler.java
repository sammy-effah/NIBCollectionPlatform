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
    // private static final Logger logger = LogManager.getLogger(CustomAuthenticationSuccessHandler.class);

    // @Override
    // public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    //         Authentication authentication) throws IOException, ServletException {
    //     Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
    //     logger.info("ROLES:: " + roles);
    //     if (roles.contains("ROLE_ADMIN")) {
    //         response.sendRedirect(request.getContextPath() + "/admin");
    //     } else if (roles.contains("ROLE_USER")) {
    //         response.sendRedirect(request.getContextPath()  + "/collection");
    //     }
    // }

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (!userRepository.existsByUsernameAndEnabled(authentication.getName(), true)) {
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            // Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            // logger.info("ROLES:: " + roles);
            if (userRepository.hasAdminRole(authentication.getName())) {
                // response.sendRedirect(request.getContextPath() + "/nia/verification");
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                response.sendRedirect(request.getContextPath()  + "/donatee");
            }
        }

    }
}
