package com.garg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garg.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

	public boolean existsByEmail(String email);
	
	public Optional<User> findByEmail(String email);
	
}
