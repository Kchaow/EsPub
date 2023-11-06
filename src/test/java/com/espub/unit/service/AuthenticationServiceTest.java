package com.espub.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.espub.dao.RoleDao;
import com.espub.dao.UserDao;
import com.espub.dto.AuthenticationRequest;
import com.espub.dto.AuthenticationResponse;
import com.espub.dto.RegisterRequest;
import com.espub.model.Role;
import com.espub.model.User;
import com.espub.service.AuthenticationService;
import com.espub.service.JwtService;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest 
{
	@Mock
	private UserDao userDao;
	@Mock
	private RoleDao roleDao;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtService jwtService;
	@Mock
	private AuthenticationManager authenticationManager;
	@InjectMocks
	AuthenticationService essayEditorService;
	private String token = "receivedToken";
	private RegisterRequest registerRequest = RegisterRequest.builder()
			.username("username")
			.password("password")
			.build();
	private AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
			.username("username")
			.password("password")
			.build();
	private AuthenticationResponse authenticationResponse = 
			AuthenticationResponse.builder()
			.token(token)
			.build();
	
	@Test
	void registerShouldReturnResponseEntityWithToken()
	{
		when(userDao.existsByUsername(Mockito.anyString())).thenReturn(false);
		Role role = Role.builder()
				.name("USER").build();
		when(roleDao.findByName(Mockito.anyString())).thenReturn(Optional.of(role));
		when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
		when(userDao.save(Mockito.any(User.class))).thenReturn(null);
		when(jwtService.generateToken(Mockito.any(User.class))).thenReturn(token);
		
		ResponseEntity<AuthenticationResponse> response = 
				essayEditorService.register(registerRequest);
		
		assertAll(
				() -> assertEquals(authenticationResponse, response.getBody()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void registerExistingUserShouldReturnConflict()
	{
		when(userDao.existsByUsername(Mockito.anyString())).thenReturn(true);
		ResponseEntity<AuthenticationResponse> response = 
				essayEditorService.register(registerRequest);
		
		assertAll(
				() -> assertEquals(null ,response.getBody()),
				() -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode())
				);
	}
	
	@Test
	void authenticateShouldReturnResponseEntityWithToken()
	{
		when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.of(new User()));
		when(jwtService.generateToken(Mockito.any(User.class))).thenReturn(token);
		
		ResponseEntity<AuthenticationResponse> response = 
				essayEditorService.authenticate(authenticationRequest);
		
		assertAll(
				() -> assertEquals(authenticationResponse, response.getBody()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void authenticateWithBadCredentialsShouldReturnForbidden()
	{
		when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Wrong credentials"));

		ResponseEntity<AuthenticationResponse> response = 
				essayEditorService.authenticate(authenticationRequest);
		
		assertAll(
				() -> assertEquals(null ,response.getBody()),
				() -> assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode())
				);
	}
}
