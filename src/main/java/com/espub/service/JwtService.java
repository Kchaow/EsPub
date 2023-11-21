package com.espub.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {
	@Value("${jwt.token.secret}")
	private String SECRET_KEY;
	@Value("${jwt.token.expired}")
	private long expired;
	Logger logger = LoggerFactory.getLogger(JwtService.class);
	
	public Optional<String> getJwtToken(HttpServletRequest request)
	{
		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer "))
			return Optional.empty();
		return Optional.of(authHeader.substring(7));
	}
	
	public String extractUsername(String token) throws UnsupportedJwtException, JwtException
	{
		return extractClaim(token, Claims::getSubject);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws UnsupportedJwtException, JwtException
	{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(UserDetails userDetails)
	{
		return Jwts
				.builder()
				.claims()
					.add(new HashMap<>())
					.subject(userDetails.getUsername())
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + expired))
					.and()
				.signWith(getSignInKey())
				.compact();			
	}
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
	{
		return Jwts
				.builder()
				.claims()
					.add(extraClaims)
					.subject(userDetails.getUsername())
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + expired))
					.and()
				.signWith(getSignInKey())
				.compact();			
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) throws UnsupportedJwtException, JwtException
	{
		final String username = extractUsername(token);//Делать валидность не по именам
		if (isTokenExpired(token))
			logger.info("User {} token has expired", userDetails.getUsername());
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) throws UnsupportedJwtException, JwtException
	{
		try
		{
			return extractExpiration(token).before(new Date());
		}
		catch (NullPointerException e)
		{
			return true;
		}	
	}
	
	private Date extractExpiration(String token) throws UnsupportedJwtException, JwtException
	{
		return extractClaim(token, Claims::getExpiration);
	}
	
	private Claims extractAllClaims(String token) throws UnsupportedJwtException, JwtException
	{
		try
		{
			return Jwts
					.parser()
					.verifyWith(getSignInKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		}
		catch (MalformedJwtException e)
		{
			logger.debug(e.getMessage());
			throw new UnsupportedJwtException(token, e);
		}
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
