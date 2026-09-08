package com.auth.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private final String SECRET_KEY = "12345678901234567890123456789012";
	
//	@Value("${jwt.secret-key}")
//	private String SECRET_KEY;
//	
	
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+86400000))
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
		
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername());
	}
}
