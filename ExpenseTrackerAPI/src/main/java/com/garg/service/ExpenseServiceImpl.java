package com.garg.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.garg.entity.Expense;
import com.garg.entity.User;
import com.garg.exception.ResourceNotFoundException;
import com.garg.repository.IExpenseRepository;

@Service
public class ExpenseServiceImpl implements IExpenseService {

	@Autowired
	private IExpenseRepository repository;
	
	// Modified expense service & repository methods with specific logged in/authenticated user details, else it will give all expenses related to all users
	@Autowired
	private IUserService userService;
	
	@Override
	public List<Expense> getAllExpenses() {
		// TODO Auto-generated method stub
		
		User loggedInUser=userService.getLoggedInUserDetails();
		Long userId=loggedInUser.getId();
		
		return repository.findByUserId(userId);		// retreive all expenses for logged in user only
		//	return repository.findAll();	retreive all expenses for all users
	}

	@Override
	public Page<Expense> getAllExpensesByPagination(Pageable pageable) {
		// TODO Auto-generated method stub
	
		User loggedInUser=userService.getLoggedInUserDetails();
		Long userId=loggedInUser.getId();
		
		return repository.findByUserId(userId, pageable);	// retreive all expenses for logged in user only
		//	return repository.findAll(pageable);	retreive all expenses for all users
	}
	
	@Override
	public Expense getExpenseById(Long id) {
		// TODO Auto-generated method stub
		
		User loggedInUser=userService.getLoggedInUserDetails();
		Long userId=loggedInUser.getId();
		
		Optional<Expense> op=repository.findByUserIdAndId(userId, id);	// retreive expense details for logged in user only
		//	Optional<Expense> op=repository.findById(id);	retreive expense details for all users 
	
		if(op.isPresent())
			return op.get();
		else
			//	throw new RuntimeException("Expense is not found for the ID : " + id);
			//	throw new ExpenseNotFoundException("Expense is not found for the ID : " + id);
			throw new ResourceNotFoundException("Expense is not found for the ID : " + id);
	}

	@Override
	public void deleteExpenseById(Long id) {
		// TODO Auto-generated method stub
		
		// give expense w.r.t. logged in user 
		Expense expense=getExpenseById(id);
		
		repository.delete(expense);
	}

	@Override
	public Expense saveExpenseDetails(Expense expense) {
		// TODO Auto-generated method stub
		
		User loggedInUser=userService.getLoggedInUserDetails();
		
		// save expense w.r.t. specific logged in user only
		expense.setUser(loggedInUser);
		
		Expense savedExpense=repository.save(expense);
		
		return savedExpense;
	}

	@Override
	public Expense updateExpenseDetails(Long id, Expense expense) {
		// TODO Auto-generated method stub
		
		// give expense w.r.t. logged in user 
		Expense existingExpense=getExpenseById(id);
		
		existingExpense.setName( (expense.getName()!=null) ? expense.getName() : existingExpense.getName() );
		existingExpense.setDescription( (expense.getDescription()!=null) ? expense.getDescription() : existingExpense.getDescription() );
		existingExpense.setAmount( (expense.getAmount()!=null) ? expense.getAmount() : existingExpense.getAmount() );
		existingExpense.setCategory( (expense.getCategory()!=null) ? expense.getCategory() : existingExpense.getCategory() );
		existingExpense.setDate( (expense.getDate()!=null) ? expense.getDate() : existingExpense.getDate() );
		
		Expense updatedExpense=repository.save(existingExpense);
		
		return updatedExpense;
	}

	@Override
	public List<Expense> getAllExpensesByCategory(String category, Pageable pageable) {
		// TODO Auto-generated method stub
		
		User loggedInUser=userService.getLoggedInUserDetails();
		Long userId=loggedInUser.getId();
		
		Page<Expense> page=repository.findByUserIdAndCategory(userId, category, pageable);	// for logged in user only
		//	Page<Expense> page=repository.findByCategory(category, pageable);	for all users
		
		return page.toList();
	}

	@Override
	public List<Expense> getAllExpensesByName(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		
		User loggedInUser=userService.getLoggedInUserDetails();
		Long userId=loggedInUser.getId();
		
		Page<Expense> page=repository.findByUserIdAndNameContaining(userId, keyword, pageable);	// for logged in user only
		//	Page<Expense> page=repository.findByNameContaining(keyword, pageable); 	for all users
		
		return page.toList();
	}

	@Override
	public List<Expense> getAllExpensesByDateRange(Date startDate, Date endDate, Pageable pageable) {
		// TODO Auto-generated method stub
		
		if(startDate==null)
			startDate=new Date(0);
		
		/*	if(endDate==null) {
			java.util.Date utilDate=new java.util.Date();
			int year=utilDate.getYear();
			int month=utilDate.getMonth();
			int day=utilDate.getMonth();
			
			endDate=new Date(year, month, day);
		}	*/
		
		User loggedInUser=userService.getLoggedInUserDetails();
		Long userId=loggedInUser.getId();
		
		Page<Expense> page=repository.findByUserIdAndDateBetween(
				userId, startDate.toLocalDate(), (endDate==null) ? LocalDate.now(): endDate.toLocalDate(), pageable);	// for logged in user
		//	Page<Expense> page=repository.findByDateBetween(startDate.toLocalDate(), endDate.toLocalDate(), pageable); 	for all users
		
		return page.toList();
	}

}
