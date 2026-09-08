package com.reservation.security;

import java.nio.charset.StandardCharsets;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private final String SECRET_KEY = "12345678901234567890123456789012";
	
	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
				.build().parseClaimsJws(token)
				.getBody();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername());
	}
	
	public String extractRole(String token) {
		return getClaims(token).get("role",String.class);
	}
}
