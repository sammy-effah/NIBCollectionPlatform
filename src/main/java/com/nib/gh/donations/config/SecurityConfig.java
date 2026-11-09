package com.nib.gh.donations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                               
                                .csrf(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .requiresChannel(ch -> ch.anyRequest().requiresSecure())
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
                                .httpBasic(); // Basic authentication
                return http.build();
        }

       
         @Bean
        public AuthenticationManager authManager() {
                return new ProviderManager(List.of(new CustomLdapAuthProvider()));
        }

        // ========= LDAP Context Source =========
        @Bean
        public LdapContextSource contextSource() {
                LdapContextSource contextSource = new LdapContextSource();
                contextSource.setUrl("ldap://NIBWS22ADC:389");
                contextSource.setBase("dc=nib,dc=com,dc=gh");
                contextSource.setReferral("ignore");
                contextSource.afterPropertiesSet();
                return contextSource;
        }

        @Bean
        public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
                return new LdapTemplate(contextSource);
        }

        // ========= PasswordEncoder =========
        @Bean
        @SuppressWarnings("deprecation")
        public PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
        }


        @Bean
        public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sslCustomizer() {
                return factory -> {
                        Ssl ssl = new Ssl();
                        ssl.setKeyStore("cfg/nib_crt.p12"); // Path to the keystore file
                        ssl.setKeyStorePassword("NIBCrT@12KEY"); // Keystore password
                        ssl.setKeyAlias("nib_crt"); // Key alias in the keystore
                        factory.setSsl(ssl);
                        factory.setPort(9900); // HTTPS port
                };
        }
}
