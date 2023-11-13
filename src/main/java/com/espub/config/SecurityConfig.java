package com.espub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.espub.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
	@Autowired
	UserService userService;
	@Autowired
	JwtAuthenticationFilter jwtAuthFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
			.authorizeHttpRequests((authorize) -> authorize
					//.requestMatchers("/essay/**").authenticated()
					.requestMatchers(
							RegexRequestMatcher.regexMatcher(HttpMethod.PATCH, "essay/\\d")
							).authenticated()
					.requestMatchers(
							RegexRequestMatcher.regexMatcher(HttpMethod.DELETE, "essay/\\d")
							).authenticated()
					.requestMatchers(HttpMethod.POST, "essay").authenticated()
					.requestMatchers("/essay/**").permitAll()
//					.requestMatchers("/essay/edit/**").hasAuthority("ADMIN")
//					.requestMatchers("/**").permitAll()
					)
			.sessionManagement((sessionManagement) -> sessionManagement
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider())
			.csrf((x)->x.disable())
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
			
		return http.build();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}
}