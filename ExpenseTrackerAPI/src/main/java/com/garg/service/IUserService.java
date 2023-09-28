package com.garg.service;

import com.garg.entity.User;
import com.garg.entity.UserModel;

public interface IUserService {

	User createUser(UserModel userModel);
	
	//	User readUser(Long id);		logged in user can access to other users details
	User readUser();	// logged in user access to logged in/authenticated user details only
	
	//	User updateUser(Long id, UserModel userModel);
	User updateUser(UserModel userModel);

	//	void deleteUser(Long id);
	void deleteUser();
	
	User getLoggedInUserDetails();
	
}
