package com.garg.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.garg.entity.User;
import com.garg.repository.IUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService  {

	@Autowired
	private IUserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<User> op=repository.findByEmail(email);	// User email is considered as username here
		
		User existingUser=op.orElseThrow( () -> new UsernameNotFoundException("User Account doesn't exist for the email : " + email) );
		
		UserDetails userDetails=
			new org.springframework.security.core.userdetails.User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>() );
		// empty ArrayList must need to pass for no user role/authority
		
		return userDetails;
	}

}
