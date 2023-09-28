package com.garg.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.garg.entity.JwtTokenModel;
import com.garg.entity.LoginModel;
import com.garg.entity.User;
import com.garg.entity.UserModel;
import com.garg.service.CustomUserDetailsService;
import com.garg.service.IUserService;
import com.garg.util.JwtTokenUtil;

// these two rest endpoints should be publicly accessible by anyone that needs no authentication for any user

// we can keep these two rest endpoints in UserController also, no need to keep separately

/* As all rest endpoints are protected that needs authentication, except /login, /register that needs no authentication
	Verify username & password user login information for the 1st time as AuthenticationFilter uses SecurityContext to check whether 
		user is logged in(authenticated) or not(not authenticated) 
	
	So, use /login Rest endpoint for 1st time authentication so to access other protected endpoints without authentication directly
	(or you can access with authentication first)& set logged in details using AuthenticationFilter SecurityContext

JWT control flow - like Session Tracking Mechanism in Servlets, just replaced JSessionID with JWT token
After login to an app, a session created b/w user and app with JSessionID generated by app to client & back to server by client on each 
request to validate user session

	1. While login first time(Authentication/Login Details),
			Email/Password to Server(2. Authenticate user in login rest endpoint & 3. Generate JWT Token/JSessionID)
	4. Return JWT/JSessionID to client(Either store in cookies/session/Storage Medium for future request)
	5. Pass JWT token/JSessionID in header i.e., key as Authorization & value as Bearer <Jwt Token Value>
		while sending each client request to rest endpoint, if don't pass server sends unauthorized error
	So, passing JWT token is more secure than JSessionID due to encryption structure.
*/

@RestController
public class LoginController {

	@Autowired
	private IUserService service;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/*	@PostMapping("/login")
	public ResponseEntity<HttpStatus> loginUser(@RequestBody LoginModel loginModel) {
		Authentication authentication=authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginModel.getEmail(), loginModel.getPassword() ) );
		
		// Saving logged in user authentication details to SecurityContext
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}	*/
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	// Step 1. Generate JwtToken after client authentication & return JwtToken to client for first time 
	@PostMapping("/login")
	public ResponseEntity<JwtTokenModel> loginUser(@RequestBody LoginModel loginModel) throws Exception {
		// client authentication
		authenticateUser(loginModel.getEmail(), loginModel.getPassword());
		
		// need authenticated user details(username & password) to generate JwtToken 
		UserDetails authenticatedUserDetails=customUserDetailsService.loadUserByUsername(loginModel.getEmail());
		
		// Step 2. JwtTokenUtil class to generate JwtToken 
		final String jwtToken=jwtTokenUtil.generateJwtToken(authenticatedUserDetails);
		
		JwtTokenModel body=new JwtTokenModel(jwtToken);
		
		// returning jwt token to client
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	private void authenticateUser(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password) );
		} catch(DisabledException e) {
			throw new Exception("User Disabled");
		} catch(BadCredentialsException e) {
			throw new Exception("Bad Credentials");
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> createNewUser(@Valid @RequestBody UserModel userModel) {
		User body=service.createUser(userModel);
	
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}
	
}