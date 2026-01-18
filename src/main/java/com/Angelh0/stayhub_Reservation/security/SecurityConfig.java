package com.Angelh0.stayhub_Reservation.security;

import com.Angelh0.stayhub_Reservation.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/h2-console-Reservation/**", "/api-docs/**").permitAll()

                        // Endpoints USER
                        .requestMatchers("/api/v1/createReservation/").hasAnyRole("user", "OWNER")

                        .anyRequest().authenticated()
                )


                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .headers(headers -> headers.frameOptions(frame -> frame.disable()));


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
