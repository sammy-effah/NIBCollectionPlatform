package com.nib.gh.donations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


        @Autowired
        private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf().disable()
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/nib/api/nia/*").authenticated()
                                                .anyRequest().authenticated())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login")
                                                .permitAll()
                                                .successHandler(authenticationSuccessHandler))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .invalidSessionUrl("/login?session=expired")
                                                .maximumSessions(1)
                                                .expiredUrl("/login?session=expired"))
                                .exceptionHandling()
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                        response.sendRedirect(request.getContextPath() + "/access-denied");
                                })
                                .and()
                                .httpBasic(); // This replaces withDefaults() for basic authentication

                return http.build();
        }

        @Bean
        public ActiveDirectoryLdapAuthenticationProvider ldapAuthenticationProvider() {
                ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
                                "nib-ghana.com", "ldap://172.17.250.26:389");
                provider.setConvertSubErrorCodesToExceptions(true);
                provider.setUseAuthenticationRequestCredentials(true);
                return provider;
        }

}
