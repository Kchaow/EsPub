package com.espub.unit.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.espub.service.EssayService;
import com.espub.service.JwtService;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest 
{
	@Mock
	HttpServletRequest httpServletRequest;
	@InjectMocks
	private JwtService jwtService;
	private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
			+ ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
			+ ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
	private String header = "Bearer " + token;
	private String subject = "John Doe";
	
	@Test
	public void getJwtTokenShouldReturnToken()
	{
		try
		{
			when(httpServletRequest.getHeader(Mockito.anyString())).thenReturn(header);
			
			Optional<String> output = jwtService.getJwtToken(httpServletRequest);
			
			assertDoesNotThrow(() -> output.get());
			assertEquals(output.get(), token);
		}
		catch(Exception e)
		{
			
		}
	}
	@Test
	public void getJwtTokenShouldReturnEmptyIfNoHeader()
	{
		when(httpServletRequest.getHeader(Mockito.anyString())).thenReturn(null);
			
		Optional<String> output = jwtService.getJwtToken(httpServletRequest);
			
		assertEquals(output.isEmpty(), true);
	}
	@Test
	public void extractClaimShouldReturnClaim()
	{
		String name = jwtService.extractClaim(token, (x) -> x.getSubject());
		
		assertEquals(name, subject);
	}
	
}


