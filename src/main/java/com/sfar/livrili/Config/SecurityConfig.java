package com.sfar.livrili.Config;


import com.sfar.livrili.Repositories.UserRepository;
import com.sfar.livrili.Security.JwtAuthenticationFilter;
import com.sfar.livrili.Security.LivriliUserDetailsService;
import com.sfar.livrili.Service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationManagerService) {
        return new JwtAuthenticationFilter(authenticationManagerService);
    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new LivriliUserDetailsService(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signUp")
                        .permitAll()
                        .requestMatchers( "/api/v1/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html").permitAll()

                        // Authenticated Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/auth").authenticated()

                        // Client Permissions
                        .requestMatchers(HttpMethod.POST, "/api/client/packs", "/api/client/packs/offer/**").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/client/packs").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/client/packs/**").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/client/packs/**").hasAuthority("CLIENT")

                        // Delivery Person Permissions
                        .requestMatchers(HttpMethod.POST, "/api/auth/testAuth").hasAuthority("DELIVERY_PERSON")
                        .requestMatchers(HttpMethod.GET, "/api/dg/pack/**").hasAuthority("DELIVERY_PERSON")
                        .requestMatchers(HttpMethod.POST, "/api/dg/pack/offer/**").hasAuthority("DELIVERY_PERSON")
                        .requestMatchers(HttpMethod.PUT, "/api/dg/pack/offer/**").hasAuthority("DELIVERY_PERSON")
                        .requestMatchers(HttpMethod.DELETE, "/api/dg/pack/offer/**").hasAuthority("DELIVERY_PERSON")


                        .anyRequest().authenticated()
                )

                .cors(cros -> cros.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow requests from your frontend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // Allow specific HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials (important if you're using cookies or authorization headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
