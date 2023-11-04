package com.espub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.espub.dto.AuthenticationRequest;
import com.espub.dto.AuthenticationResponse;
import com.espub.dto.RegisterRequest;
import com.espub.service.AuthenticationService;

@RestController
@RequestMapping("api")
public class AuthenticationController 
{
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
	{
		return new ResponseEntity<>(authenticationService.register(request), HttpStatus.OK);
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request)
	{
		return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
	}
}
