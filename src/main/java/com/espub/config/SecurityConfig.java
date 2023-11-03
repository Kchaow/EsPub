package com.espub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.espub.service.UserService;


class SecurityInitializer extends AbstractSecurityWebApplicationInitializer
{
	@Override
	protected boolean enableHttpSessionEventPublisher()
	{
		return true;
	}
}

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
	@Autowired
	UserService userService;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers("/essay/edit/**").hasRole("admin")
					.requestMatchers("/**").permitAll()
					)
					.httpBasic(Customizer.withDefaults())
					.userDetailsService(userService)
					//.formLogin(Customizer.withDefaults())
					.logout(Customizer.withDefaults());
		return http.build();
	}
	
	@Bean
	static PasswordEncoder passwordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}
}