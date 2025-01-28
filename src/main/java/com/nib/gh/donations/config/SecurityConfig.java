package com.nib.gh.donations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf().disable()
                                .requiresChannel(channel -> channel.anyRequest().requiresSecure()) // Enforces HTTPS
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
        public ActiveDirectoryLdapAuthenticationProvider ldapAuthenticationProvider() {
                ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
                                "nib-ghana.com", "ldap://172.17.250.26:389");
                provider.setConvertSubErrorCodesToExceptions(true);
                provider.setUseAuthenticationRequestCredentials(true);
                return provider;
        }

        @Bean
        public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sslCustomizer() {
                return factory -> {
                        Ssl ssl = new Ssl();
                        ssl.setKeyStore("cfg/keystore.jks"); // Path to the keystore file
                        ssl.setKeyStorePassword("172.17.1.8P@ssw0rd"); // Keystore password
                        ssl.setKeyAlias("1.8cert"); // Key alias in the keystore
                        factory.setSsl(ssl);
                        factory.setPort(9900); // HTTPS port
                };
        }
}
