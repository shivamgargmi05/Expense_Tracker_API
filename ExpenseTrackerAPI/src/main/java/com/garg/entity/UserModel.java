package com.garg.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

	@NotBlank(message="User name can't be null or empty or blank")
	private String name;
	
	@NotBlank(message="Email must not be null or empty or blank")
	@Email(message="Please provide valid email")
	private String email;
	
	@NotBlank(message="Password must not be null or empty or blank")
	@Size(min=8, message="Password must have atleast 8 characters")
	private String password;
	
	private Long age=0L;	// field injection

}
