// package com.nib.gh.donations.config;

// import com.nib.gh.donations.services.CustomUserDetailsService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.boot.web.server.Ssl;
// import org.springframework.boot.web.server.WebServerFactoryCustomizer;
// import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfigDB {

//     @Autowired
//     private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

//     @Autowired
//     private CustomUserDetailsService customUserDetailsService;

//     // ==================================================================
//     // UPDATED: Main Security Configuration
//     // ==================================================================
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable())
//                 .requiresChannel(channel -> channel.anyRequest().requiresSecure())
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/nib/api/nia/*").authenticated() // Example secured path
//                         .requestMatchers("/login", "/css/**", "/js/**").permitAll() // Allow access to login and static
//                                                                                     // resources
//                         .anyRequest().authenticated())
//                 .formLogin(formLogin -> formLogin
//                         .loginPage("/login")
//                         .permitAll()
//                         .successHandler(authenticationSuccessHandler))
//                 .logout(logout -> logout
//                         .logoutUrl("/logout")
//                         .logoutSuccessUrl("/login?logout")
//                         .invalidateHttpSession(true)
//                         .deleteCookies("JSESSIONID")
//                         .permitAll())
//                 .sessionManagement(session -> session
//                         .invalidSessionUrl("/login?session=expired")
//                         .maximumSessions(1)
//                         .expiredUrl("/login?session=expired"))
//                 .exceptionHandling(exceptions -> exceptions
//                         .accessDeniedHandler((request, response, accessDeniedException) -> {
//                             response.sendRedirect(request.getContextPath() + "/access-denied");
//                         }));

//         // Register our custom authentication provider
//         http.authenticationProvider(authenticationProvider());

//         return http.build();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() { // New Bean
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(customUserDetailsService); // Set custom UserDetailsService
//         authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder
//         return authProvider;
//     }

//     @Bean
//     public BCryptPasswordEncoder passwordEncoder() { // New Bean
//         return new BCryptPasswordEncoder();
//     }

//     // ==================================================================
//     // SSL Configuration (Unchanged)
//     // ==================================================================
//     @Bean
//     public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sslCustomizer() {
//         return factory -> {
//             Ssl ssl = new Ssl();
//             ssl.setKeyStore("cfg/keystore.jks");
//             ssl.setKeyStorePassword("172.17.1.8P@ssw0rd");
//             ssl.setKeyAlias("1.8cert");

//             factory.setSsl(ssl);
//             factory.setPort(9900);
//         };
//     }
// }
