package com.garg.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	// to customize default exception message, null - value not included, empty - "", blank - doesn't allow only spaces
	@NotBlank(message="Expense name must not be null or empty or blank")	
	@Size(min=3, message="Expense name must have atleast 3 characters")
	private String name;
	
	private String description;
	
	@NotNull(message="Expense amount must not be null")
	private Double amount;

	@NotBlank(message="Expense category must not be null or empty or blank")
	private String category;
	
	@NotNull(message="Expense date must not be null")
	private LocalDate date;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="userId", referencedColumnName="id")
	@OnDelete(action=OnDeleteAction.CASCADE)	// on user deletion, delete user related expenses as well
	@JsonIgnore		// while converting expense java object to expense json object, hide user details on retreiving expense details
	private User user;	// Multiple expenses are linked to an user
	
}
