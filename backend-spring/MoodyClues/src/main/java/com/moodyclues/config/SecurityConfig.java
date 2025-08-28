package com.moodyclues.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
								.requestMatchers("/api/user/login", "/api/user/register",
										"/api/counsellor/login", "/api/counsellor/register").permitAll()


//	        .requestMatchers(HttpMethod.POST, "/api/journal/submit").permitAll()
//	        .requestMatchers(HttpMethod.GET, "/api/journal/all/*").permitAll()
//	        
//	        .requestMatchers(HttpMethod.GET, "/api/journal/entries").permitAll() 
//	        .requestMatchers(HttpMethod.GET, "/api/journal/entries/*").permitAll()
//
//	        .requestMatchers(HttpMethod.POST, "/api/journal/entries").permitAll()
//	        .requestMatchers(HttpMethod.PUT, "/api/journal/entries/*/archive").permitAll()
//
//	        .requestMatchers(HttpMethod.GET, "/api/habits/all/*").permitAll()
//	        .requestMatchers(HttpMethod.GET, "/api/habits//").permitAll()
//
//	        .requestMatchers(HttpMethod.POST, "/api/habits/submit").permitAll()
//	        .requestMatchers(HttpMethod.PUT, "/api/habits///edit").permitAll()
//	        .requestMatchers(HttpMethod.PUT, "/api/habits///archive").permitAll()

								.requestMatchers("/api/journal/*", "/api/habits/*").permitAll()
								.anyRequest().permitAll()
				)
				.httpBasic(b -> b.disable())
				.formLogin(f -> f.disable());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(Arrays.asList("http://localhost:5178"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

}