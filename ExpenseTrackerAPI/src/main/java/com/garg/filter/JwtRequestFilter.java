package com.garg.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.garg.service.CustomUserDetailsService;
import com.garg.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Step 3. Create a JwtRequestFilter class to validate JwtToken sent by client with request from 2nd time

public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		final String requestTokenHeader=request.getHeader("Authorization");
		
		String requestJwtToken=null;
		String username=null;
		
		if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ") ) {
			requestJwtToken=requestTokenHeader.substring(7);
		
			try {
				username=jwtTokenUtil.getUsernameFromJwtToken(requestJwtToken);
			} catch(IllegalArgumentException e) {
				throw new RuntimeException("Unable to get JWT Token");
			} catch(ExpiredJwtException e) {
				throw new RuntimeException("JWT Token has expired");
			}
		}
		
		// In Spring Security, Authentication Filter uses SecurityContext for saving logged in/authenticated user details 
		// once we get the token from the request, validate it
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null ) {
			UserDetails userDetails=userDetailsService.loadUserByUsername(username);
			
			if(jwtTokenUtil.validateJwtToken(requestJwtToken, userDetails) ) {	// if user is authenticated
				UsernamePasswordAuthenticationToken authToken=
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
			
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request) );
				
				// save authenticated user in Spring Security SecurityContext uses by Authentication Filter
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		filterChain.doFilter(request, response);	// continue Spring Security Authentication Filter
	}

}
