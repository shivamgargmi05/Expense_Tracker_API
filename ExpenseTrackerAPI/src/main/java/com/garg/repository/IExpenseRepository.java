package com.garg.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.garg.entity.Expense;

// Spring Data JPA will generate SQL queries for finder query methods automatically in implementation class i.e., findByPropertyCondition()

public interface IExpenseRepository extends JpaRepository<Expense, Long> {

	//	public Page<Expense> findByCategory(String category, Pageable pageable);	for all users
	public Page<Expense> findByUserIdAndCategory(Long userId, String category, Pageable pageable);	// specific to logged in/authenticated user
	
	//	public Page<Expense> findByNameContaining(String keyword, Pageable pageable);
	public Page<Expense> findByUserIdAndNameContaining(Long userId, String keyword, Pageable pageable);
	
	//	public Page<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
	public Page<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	// List<Expense> findAll(); inbuilt
	public List<Expense> findByUserId(Long userId);		
	
	// Page<Expense> findAll(Pageable pageable); inbuilt
	public Page<Expense> findByUserId(Long userId, Pageable pageable);	
	
	// Optional<Expense> findById(Long id); inbuilt
	public Optional<Expense> findByUserIdAndId(Long userId, Long expenseId);	
	
}
