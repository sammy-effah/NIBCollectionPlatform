
package com.nib.gh.donations.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT username, password, role FROM users WHERE username = ?";
        
        List> users = jdbcTemplate.queryForList(sql, username);
        
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        Map<String, Object> userRow = users.get(0);
        String dbUsername = (String) userRow.get("username");
        String dbPassword = (String) userRow.get("password");
        String role = userRow.get("role") != null ? (String) userRow.get("role") : "ROLE_USER";

        // Ensure role has the ROLE_ prefix required by Spring Security
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return new User(
                dbUsername,
                dbPassword,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
