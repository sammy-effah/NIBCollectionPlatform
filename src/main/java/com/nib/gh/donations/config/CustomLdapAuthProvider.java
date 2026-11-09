package com.nib.gh.donations.config;

import java.util.List;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


public class CustomLdapAuthProvider implements AuthenticationProvider {
    private static final String[] DOMAINS = {
            "nib.com.gh",
            "nib-ghana.com"
    };

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // If username already has @domain, just try it directly
        if (username.contains("@")) {
            if (bindAuth(username, password)) {
                return buildAuthToken(username, password);
            }
        } else {
            // Try each allowed domain
            for (String domain : DOMAINS) {
                String upn = username + "@" + domain;
                if (bindAuth(upn, password)) {
                    return buildAuthToken(upn, password);
                }
            }
        }

        throw new BadCredentialsException("Invalid credentials for all configured domains");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean bindAuth(String userDn, String password) {
        try {
            LdapContextSource tempSource = new LdapContextSource();
            tempSource.setUrl("ldap://NIBWS22ADC:389");
            tempSource.setBase("dc=nib,dc=com,dc=gh");
            tempSource.setUserDn(userDn);
            tempSource.setPassword(password);
            tempSource.afterPropertiesSet();

            tempSource.getReadOnlyContext().close(); // successful bind
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Authentication buildAuthToken(String username, String password) {
        return new UsernamePasswordAuthenticationToken(
                username,
                password,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
