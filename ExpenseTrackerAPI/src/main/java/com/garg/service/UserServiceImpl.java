package com.garg.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garg.entity.User;
import com.garg.entity.UserModel;
import com.garg.exception.ResourceNotFoundException;
import com.garg.exception.UserEmailExistsException;
import com.garg.repository.IUserRepository;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserModel userModel) {
		// TODO Auto-generated method stub
		
		User user=new User();
		
		/*	user.setName(userModel.getName());
		user.setEmail(userModel.getEmail());
		user.setPassword(userModel.getPassword());
		user.setAge(userModel.getAge());	*/
		BeanUtils.copyProperties(userModel, user);
		;
		boolean exists=repository.existsByEmail(user.getEmail());
		if(exists) 
			//	throw new RuntimeException("User Email Already Exists : " + user.getEmail() );
			throw new UserEmailExistsException("User Email Already Exists : " + user.getEmail() );
		
		// encoding password to save in db in encrypted format instead of saving/updating in plain text
		user.setPassword(passwordEncoder.encode(userModel.getPassword()) );
		
		User savedUser=repository.save(user);
		
		return savedUser;
	}

	@Override
	public User readUser() {
		// TODO Auto-generated method stub
		
		User loggedInUser=getLoggedInUserDetails();
		Long id=loggedInUser.getId();
		
		Optional<User> op=repository.findById(id);
		
		User user=op.orElseThrow( () -> new ResourceNotFoundException("User is not found for the ID : " + id) );
	
		return user;
	}

	@Override
	public User updateUser(UserModel userModel) {
		// TODO Auto-generated method stub
				
		// give logged in/authenticated user only
		User existingUser=readUser();
		
		existingUser.setName( (userModel.getName()!=null) ? userModel.getName() : existingUser.getName() );
		existingUser.setEmail( (userModel.getEmail()!=null) ? userModel.getEmail() : existingUser.getEmail() );
		existingUser.setPassword( (userModel.getPassword()!=null) ? passwordEncoder.encode(userModel.getPassword() ) : existingUser.getPassword() );
		existingUser.setAge( (userModel.getAge()!=null) ? userModel.getAge() : existingUser.getAge() );;
		
		User updatedUser=repository.save(existingUser);
		
		return updatedUser;
	}

	@Override
	public void deleteUser() {
		// TODO Auto-generated method stub
		
		// give logged in/authenticated user only
		User user=readUser();
		
		repository.delete(user);
	}

	@Override
	public User getLoggedInUserDetails() {
		// TODO Auto-generated method stub
		
		// Getting Logged in user authentication details stored in SecurityContext while login
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		
		String email=authentication.getName();
		
		Optional<User> op=repository.findByEmail(email);
		
		User user=op.orElseThrow( () -> new UsernameNotFoundException("User Account doesn't exist for the email : " + email) );
		
		return user;
	}

}
