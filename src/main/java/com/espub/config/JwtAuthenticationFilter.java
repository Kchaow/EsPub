package com.espub.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.espub.service.JwtService;
import com.espub.service.UserService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserService userService;
	Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		try 
		{
			final String jwt;
			final String username;
			jwt = jwtService.getJwtToken(request).orElse(null);
			if (jwt == null)
			{
				filterChain.doFilter(request, response);
				return;
			}
			logger.info("Token received from {}, authentication attempt", request.getRemoteAddr());
			username = jwtService.extractUsername(jwt);
			logger.info("Token has a trusted signature");
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
			{
				UserDetails userDetails = userService.loadUserByUsername(username);
				if (jwtService.isTokenValid(jwt, userDetails))
				{
					UsernamePasswordAuthenticationToken authToken =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			
			filterChain.doFilter(request, response);
		}
		catch (IllegalArgumentException | JwtException e)
		{
			logger.error("{}: Failed to verify token", e.toString());
			logger.warn("Request with unverified signature from address {}", request.getRemoteAddr());
			filterChain.doFilter(request, response);
		}
	}
}
