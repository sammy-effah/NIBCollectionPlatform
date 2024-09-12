// package com.nib.gh.donations.config;

// import java.util.Collection;

// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import com.nib.gh.donations.services.CustomUserDetailsService;

// public class CustomAuthenticationProvider implements AuthenticationProvider {

//     private final UserDetailsService userDetailsService;

//     public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
//         this.userDetailsService = userDetailsService;
//     }

//     @Override
//     public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//         String username = authentication.getName();
//         String password = (String) authentication.getCredentials();
//         Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

//         CustomUserDetailsService userDetails = (CustomUserDetailsService) userDetailsService.loadUserByUsername(username);

//         return new UsernamePasswordAuthenticationToken(username, password, authorities);
//     }

//     @Override
//     public boolean supports(Class<?> authentication) {
//         return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//     }
// }

