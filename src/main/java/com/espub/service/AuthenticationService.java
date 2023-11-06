package com.espub.service;

import java.util.ArrayList;
import java.util.List;

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
import com.espub.model.Role;
import com.espub.model.User;

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
	
	public ResponseEntity<AuthenticationResponse> register(RegisterRequest registerRequest)
	{
		if (userDao.existsByUsername(registerRequest.getUsername()))
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		
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
		var jwtToken = jwtService.generateToken(user);
		return new ResponseEntity<>(
					AuthenticationResponse.builder()
					.token(jwtToken)
					.build(),
					HttpStatus.OK
				);
	}
	
	public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authRequest)
	{
		try
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			User user = userDao.findByUsername(authRequest.getUsername()).orElseThrow();
			var jwtToken = jwtService.generateToken(user);
			return new ResponseEntity<>(
						AuthenticationResponse.builder()
						.token(jwtToken)
						.build(),
						HttpStatus.OK
					);
		}
		catch (BadCredentialsException e)
		{
			//TODO залогировать
			return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
		}
		
	}
}
