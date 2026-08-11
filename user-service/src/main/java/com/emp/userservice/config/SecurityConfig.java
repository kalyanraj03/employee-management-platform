package com.emp.userservice.config;

import com.emp.userservice.security.JwtAccessDeniedHandler;
import com.emp.userservice.security.JwtAuthenticationEntryPoint;
import com.emp.userservice.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

         http.csrf(csrf -> csrf.disable())
                 .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/api/v1/auth/**")
                         .permitAll()

                         .requestMatchers(HttpMethod.GET).permitAll()

                         .requestMatchers(HttpMethod.POST,
                                 "/api/v1/users/**")
                         .hasRole("ADMIN")

                         .requestMatchers(HttpMethod.PUT,
                                 "/api/v1/users/**")
                         .hasRole("ADMIN")

                         .requestMatchers(HttpMethod.DELETE,
                                 "/api/v1/users/**")
                         .hasRole("ADMIN")

                         .anyRequest()
                         .authenticated())
                 .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS
                        ))
                 .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                ).exceptionHandling(exception ->
                         exception.authenticationEntryPoint(authenticationEntryPoint)
                                 .accessDeniedHandler(jwtAccessDeniedHandler)
                 );

         return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
}