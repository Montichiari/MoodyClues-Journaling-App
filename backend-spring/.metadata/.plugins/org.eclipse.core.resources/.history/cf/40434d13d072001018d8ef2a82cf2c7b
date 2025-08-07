package com.moodyclues.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/register", "/api/user/login", "/api/counsellor/login", "/api/counsellor/register").permitAll()
                .requestMatchers("/api/journal/**").hasRole("JOURNAL")
                .requestMatchers("/api/counsellor/**").hasRole("COUNSELLOR")
                .requestMatchers("/api/habits/**").hasRole("JOURNAL")
                .requestMatchers("/api/user/**").hasRole("JOURNAL")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}