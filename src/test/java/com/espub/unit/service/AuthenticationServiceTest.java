package com.espub.unit.service;

import org.junit.jupiter.api.Disabled;
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
import com.espub.exception.AlreadyExistingUsername;
import com.espub.model.Role;
import com.espub.model.User;
import com.espub.service.AuthenticationService;
import com.espub.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest 
{
	@Mock
	UserDao userDao;
	@Mock
	RoleDao roleDao;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	JwtService jwtService;
	@Mock
	AuthenticationManager authenticationManager;
	@Mock
	HttpServletRequest request;
	@InjectMocks
	AuthenticationService essayEditorService;
	
	String token = "receivedToken";
	RegisterRequest registerRequest = RegisterRequest.builder()
			.username("username")
			.password("password")
			.build();
	AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
			.username("username")
			.password("password")
			.build();
	AuthenticationResponse authenticationResponse = 
			AuthenticationResponse.builder()
			.token(token)
			.build();
	Role role = Role.builder()
			.name("USER").build();
	
	@Test
	void registerNonExistingUserShouldReturnAuthenticationResponse() throws AlreadyExistingUsername
	{
		when(userDao.existsByUsername(Mockito.anyString())).thenReturn(false);
		when(roleDao.findByName(Mockito.anyString())).thenReturn(Optional.of(role));
		when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
		when(userDao.save(Mockito.any(User.class))).thenReturn(new User());
		when(jwtService.generateToken(Mockito.any(User.class))).thenReturn(token);
		
		ResponseEntity<AuthenticationResponse> response = 
				essayEditorService.register(registerRequest);
		
		assertAll(
				() -> assertEquals(authenticationResponse, response.getBody()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void registerExistingUserShouldReturnAuthenticationResponse() throws AlreadyExistingUsername
	{
		when(userDao.existsByUsername(Mockito.anyString())).thenReturn(true);
		
		assertThrowsExactly(AlreadyExistingUsername.class, () -> essayEditorService.register(registerRequest));
	}
	
	@Test
	void authenticateExistingUserShouldReturnToken()
	{
		when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(Optional.of(new User()));
		when(jwtService.generateToken(Mockito.any(User.class))).thenReturn(token);
		when(request.getRemoteAddr()).thenReturn("ip_addr");
		
		ResponseEntity<AuthenticationResponse> response = 
				essayEditorService.authenticate(authenticationRequest);
		
		assertAll(
				() -> assertEquals(authenticationResponse, response.getBody()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode())
				);
	}
	
	@Test
	void authenticateWithBadCredentialsShouldThrowBadCredentialsException()
	{
		when(request.getRemoteAddr()).thenReturn("ip_addr");
		when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Wrong credentials"));

		assertThrowsExactly(BadCredentialsException.class, () -> essayEditorService.authenticate(authenticationRequest));
	}
}
