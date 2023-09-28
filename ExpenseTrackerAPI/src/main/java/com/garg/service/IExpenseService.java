package com.garg.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.garg.entity.Expense;

public interface IExpenseService {

	List<Expense> getAllExpenses();
	
	Page<Expense> getAllExpensesByPagination(Pageable pageable);
	
	Expense getExpenseById(Long id);
	
	void deleteExpenseById(Long id);
	
	Expense saveExpenseDetails(Expense expense);
	
	Expense updateExpenseDetails(Long id, Expense expense);
	
	List<Expense> getAllExpensesByCategory(String category, Pageable pageable);
	
	List<Expense> getAllExpensesByName(String keyword, Pageable pageable);
	
	List<Expense> getAllExpensesByDateRange(Date startDate, Date endDate, Pageable pageable);
	
}
