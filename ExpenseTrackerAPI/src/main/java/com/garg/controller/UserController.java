package com.garg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.garg.entity.User;
import com.garg.entity.UserModel;
import com.garg.service.IUserService;

@RestController
public class UserController {

	@Autowired
	private IUserService service;
	
	/*	@PostMapping("/register")
	public ResponseEntity<User> createUser(@Valid @RequestBody UserModel userModel) {
		User body=service.createUser(userModel);
		
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}	*/
	
	/* Modified User rest endpoints without explicit id in @PathVariable(get user id from logged in/authenticated user), so that logged 
	   in user can have access to only logged in user details, not access to other user details
		
	@GetMapping("/users/{id}")
	public ResponseEntity<User> readUser(@PathVariable Long id) {
		User body=service.readUser(id);
	
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserModel userModel) {
		User body=service.updateUser(id, userModel);
	
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
		service.deleteUser(id);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}	*/
	
	@GetMapping("/profile")
	public ResponseEntity<User> readUser() {
		User body=service.readUser();
	
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@PutMapping("/profile")
	public ResponseEntity<User> updateUser(@RequestBody UserModel userModel) {
		User body=service.updateUser(userModel);
	
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	@DeleteMapping("/deactivate")
	public ResponseEntity<HttpStatus> deleteUser() {
		service.deleteUser();
		
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	
}
