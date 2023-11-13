package com.espub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espub.dto.AuthenticationRequest;
import com.espub.dto.AuthenticationResponse;
import com.espub.dto.RegisterRequest;
import com.espub.exception.AlreadyExistingUsername;
import com.espub.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class AuthenticationController 
{
	@Autowired
	private AuthenticationService authenticationService;
	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) throws AlreadyExistingUsername
	{
		logger.debug("Register method from AuthenticationController received a request");
		return authenticationService.register(request);
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authRequest)
	{
		logger.debug("Authenticate method from AuthenticationController received a request");
		return authenticationService.authenticate(authRequest);
	}
}
