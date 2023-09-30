package com.garg.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Expense_User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(unique=true)	// to avoid duplicate email id w.r.t user
	private String email;
	
	// or avoids to map password value while converting json object to java object on request from client to server, so UserModel class is needed
	@JsonIgnore		// to avoid to print password to user back while converting java object to json object on response from server to client
	private String password;
	
	private Long age;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	//  @OnetoMany
	//	private List<Expense> expenses; a user can have multiple expenses, not mapping here as don't want to retreive expenses details along with fetching user details, so not using Bidirectional Mapping
}
