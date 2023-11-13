package com.espub.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.espub.dao.RoleDao;
import com.espub.dao.UserDao;
import com.espub.dto.AuthenticationRequest;
import com.espub.dto.AuthenticationResponse;
import com.espub.dto.RegisterRequest;
import com.espub.exception.AlreadyExistingUsername;
import com.espub.model.Role;
import com.espub.model.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService 
{
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private HttpServletRequest request;
	Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	public ResponseEntity<AuthenticationResponse> register(RegisterRequest registerRequest) throws AlreadyExistingUsername
	{
		if (userDao.existsByUsername(registerRequest.getUsername()))
			throw new AlreadyExistingUsername(registerRequest.getUsername());
		
		String roleName = "USER";
		Role role = roleDao.findByName(roleName).get();
		List<Role> roleList = new ArrayList<>();
		roleList.add(role);
		User user = User.builder()
				.username(registerRequest.getUsername())
				.password(passwordEncoder.encode(registerRequest.getPassword()))
				.role(roleList)
				.build();
		userDao.save(user);
		logger.debug("A record has been written to the database: {}", user.toString());
		var jwtToken = jwtService.generateToken(user);
		logger.info("User {} has been succesfully registered", user.getUsername());
		logger.info("A token has been issued to user {}", user.getUsername());
		return new ResponseEntity<>(
					AuthenticationResponse.builder()
					.token(jwtToken)
					.build(),
					HttpStatus.OK
				);
	}
	
	public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authRequest) throws BadCredentialsException
	{
		logger.info("Authentication attempt from address {}", request.getRemoteAddr());
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		logger.info("The user {} has succesfully authenticated", authRequest.getUsername());
		User user = userDao.findByUsername(authRequest.getUsername()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		logger.info("A token has been issued to user {}", user.getUsername());
		return new ResponseEntity<>(
					AuthenticationResponse.builder()
					.token(jwtToken)
					.build(),
					HttpStatus.OK
				);
	}
}
