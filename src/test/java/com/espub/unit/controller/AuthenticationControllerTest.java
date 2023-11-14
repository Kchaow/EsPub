package com.espub.unit.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.hamcrest.CoreMatchers;

import com.espub.dto.AuthenticationRequest;
import com.espub.dto.AuthenticationResponse;
import com.espub.dto.RegisterRequest;
import com.espub.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Disabled("Я хуй знает")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthenticationControllerTest 
{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private ObjectMapper objectMapper;
	@MockBean
	private AuthenticationService authenticationService;
	private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
			+ ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
			+ ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
	
	@Test
	void registerShouldReturnToken() throws Exception
	{
		AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
					.token(token)
				.build();
		RegisterRequest request = RegisterRequest.builder()
					.username("user")
					.password("password")
				.build();
		
		when(authenticationService.register(any())).thenReturn(new ResponseEntity<>(authenticationResponse, HttpStatus.OK));
		
		ResultActions response = mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect
				(
						MockMvcResultMatchers.jsonPath
						(
								"$.token", 
								CoreMatchers.is(token)
						)
				);
	}
	
	@Test
	void authenticateShouldReturnToken() throws Exception
	{
		AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
					.token(token)
				.build();
		AuthenticationRequest request = AuthenticationRequest.builder()
					.username("user")
					.password("password")
				.build();
		
		when(authenticationService.authenticate(any())).thenReturn(new ResponseEntity<>(authenticationResponse, HttpStatus.OK));
		
		ResultActions response = mockMvc.perform(post("/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect
				(
						MockMvcResultMatchers.jsonPath
						(
								"$.token", 
								CoreMatchers.is(token)
						)
				);
	}
	
	@Test
	void registerExistingUsernameShouldReturnConflict() throws Exception
	{
		RegisterRequest request = RegisterRequest.builder()
					.username("user")
					.password("password")
				.build();
		
		when(authenticationService.register(any())).thenReturn(new ResponseEntity<>(null, HttpStatus.CONFLICT));
		
		ResultActions response = mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));
		
		response.andExpect(MockMvcResultMatchers.status().isConflict());
	}
	
	@Test
	void badAuthenticateShouldReturnForbidden() throws Exception
	{
		AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
					.token(token)
				.build();
		AuthenticationRequest request = AuthenticationRequest.builder()
					.username("user")
					.password("password")
				.build();
		
		when(authenticationService.authenticate(any())).thenReturn(new ResponseEntity<>(authenticationResponse, HttpStatus.FORBIDDEN));
		
		ResultActions response = mockMvc.perform(post("/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));
		
		response.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
