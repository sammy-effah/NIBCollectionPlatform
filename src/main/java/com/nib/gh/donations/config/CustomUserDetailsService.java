package com.nib.gh.donations.config;

import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Note: If your roles table is named "roles" (plural), change "role r" to "roles r"
        String sql = """
                SELECT u.username, u.password, u.enabled, r.name AS role_name
                FROM users u
                LEFT JOIN role r ON u.role_id = r.id
                WHERE u.username = ?
                """;

        List<UserDetails> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            String dbUsername = rs.getString("username");
            String dbPassword = rs.getString("password");
            boolean enabled = rs.getBoolean("enabled");
            String role = rs.getString("role_name");

            if (role == null || role.isBlank()) {
                role = "ROLE_USER";
            } else if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }

            return User.withUsername(dbUsername)
                    .password(dbPassword)
                    .disabled(!enabled)
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                    .build();
        }, username);

        if (results.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return results.get(0);
    }
}
