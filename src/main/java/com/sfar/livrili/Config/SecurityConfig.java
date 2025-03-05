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
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/auth/signUp").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/auth").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/auth/testAuth").hasAuthority("DELIVERY_PERSON")
                                .requestMatchers(HttpMethod.POST,"/api/client/packs").hasAuthority("CLIENT")
                                .requestMatchers(HttpMethod.GET,"/api/client/packs").hasAuthority("CLIENT")
                                .requestMatchers(HttpMethod.PUT,"/api/client/packs/**").hasAuthority("CLIENT")
                                .requestMatchers(HttpMethod.DELETE,"/api/client/packs/**").hasAuthority("CLIENT")
                                .requestMatchers(HttpMethod.GET,"/api/DG/pack/**").hasAuthority("DELIVERY_PERSON")
                                .requestMatchers(HttpMethod.POST,"/api/DG/pack/offer/**").hasAuthority("DELIVERY_PERSON")
                                .requestMatchers(HttpMethod.PUT,"/api/DG/pack/offer/**").hasAuthority("DELIVERY_PERSON")
                                .requestMatchers(HttpMethod.DELETE,"/api/DG/pack/offer/**").hasAuthority("DELIVERY_PERSON")





                        .anyRequest().authenticated()
                )

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

}
