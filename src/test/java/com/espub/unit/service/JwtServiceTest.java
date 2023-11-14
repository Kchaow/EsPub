package com.espub.unit.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.espub.model.User;
import com.espub.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest 
{
	@Mock
	HttpServletRequest httpServletRequest;
	@Mock
	User user;
	@InjectMocks
	private JwtService jwtService;
	private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
			+ ".eyJzdWIiOiJKb2huIERvZSJ9"
			+ ".dmV--DEQekQoB33BQqwalfD_TaboPTrynVMgqbkyj_M";
	private String badToken = "badToken";
	private String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
			+ "eyJleHAiOjEsInN1YiI6IkpvaG4gRG9lIn0."
			+ "rhTUbYFBSvHwzMM7cnC11wNxyco7OAvOFoEtx5qAMlc";
	private String header = "Bearer " + token;
	private String subject = "John Doe";
	
	@Test
	public void getJwtTokenShouldReturnToken()
	{
			when(httpServletRequest.getHeader(Mockito.anyString())).thenReturn(header);
			
			Optional<String> output = jwtService.getJwtToken(httpServletRequest);
			
			assertDoesNotThrow(() -> output.get());
			assertEquals(output.get(), token);
	}
	@Test
	public void getJwtTokenShouldReturnEmptyIfNoHeaderAuthorization()
	{
		when(httpServletRequest.getHeader(Mockito.anyString())).thenReturn(null);
			
		Optional<String> output = jwtService.getJwtToken(httpServletRequest);
			
		assertEquals(output.isEmpty(), true);
	}
	@Test
	public void getJwtTokenShouldReturnEmptyIfNotJwtToken()
	{
		when(httpServletRequest.getHeader(Mockito.anyString())).thenReturn("anotherToken");
			
		Optional<String> output = jwtService.getJwtToken(httpServletRequest);
			
		assertEquals(output.isEmpty(), true);
	}
	@Test
	public void extractClaimShouldReturnClaim()
	{
		String name = jwtService.extractClaim(token, (x) -> x.getSubject());
		
		assertEquals(name, subject);
	}
	@Test
	public void extractUsernameShouldReturnClaimWithName()
	{
		String name = jwtService.extractUsername(token);
		
		assertEquals(name, subject);
	}
	@Test
	public void extractJwtWithbadSignatureOrClaimShouldThrowUnsupportedJwtException()
	{
		assertThrowsExactly(UnsupportedJwtException.class, () -> jwtService.extractClaim(badToken, (x) -> x.getSubject()));
		assertThrowsExactly(UnsupportedJwtException.class, () -> jwtService.extractUsername(badToken));
	}
	@Test
	public void extractClaimAndExtractUsernameFromExpiredTokenShouldThrowExpiredJwtExceptionException()
	{
		assertThrowsExactly(ExpiredJwtException.class, () -> jwtService.extractClaim(expiredToken, (x) -> x.getSubject()));
		assertThrowsExactly(ExpiredJwtException.class, () -> jwtService.extractUsername(expiredToken));
	}
	@Test
	public void generateTokenShouldntThrowInvalidKeyException()
	{
		when(user.getUsername()).thenReturn(subject);
		Map<String, Object> map = new HashMap<>();
		map.put("claim", "claimValue");
		
		assertDoesNotThrow(() -> jwtService.generateToken(user));
		assertDoesNotThrow(() -> jwtService.generateToken(map ,user));
	}
}


