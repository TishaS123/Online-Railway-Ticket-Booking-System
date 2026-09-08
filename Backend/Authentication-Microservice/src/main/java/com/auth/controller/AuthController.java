package com.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.entity.AuthRequest;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.repo.UserRepository;
import com.auth.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user){
		user.setRole(Role.PASSENGER);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return ResponseEntity.ok(userRepo.save(user));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest req){
		authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
		UserDetails user = userRepo.findByUsername(req.getUsername()).orElseThrow();
		String jwt = jwtService.generateToken(user);
		return ResponseEntity.ok(Map.of("token",jwt));
	}
}
