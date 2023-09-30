package com.garg.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garg.entity.Expense;
import com.garg.service.IExpenseService;

import jakarta.validation.Valid;

@RestController
public class ExpenseController {

	@Autowired
	private IExpenseService service;
	
	@GetMapping("/expenses")
	public List<Expense> getAllExpenses() {
		return service.getAllExpenses();
	}

	// {{baseUrl}}/expenses/pagination?size=3&page=1 as size, page & sort are properties of Pageable(I) implementation class object - protected PageRequest(int page, int size, Sort sort) { ... }
	// {{baseUrl}}/expenses/pagination?sort=name,DESC
	// {{baseUrl}}/expenses/pagination?size=3&page=1&sort=name,DESC/ASC
	// {{baseUrl}}/expenses/pagination
	@GetMapping("/expenses/pagination")		
	public List<Expense> getAllExpensesByPagination(Pageable pageable) {
		//	System.out.println("Pageable(I) Implementation class Object : " + pageable.getClass().getName() );
		
		Page<Expense> page=service.getAllExpensesByPagination(pageable);
	
		//	System.out.println("Page(I) Implementation class Object : " + page.getClass().getName() );
		
		// as Page object has metadata that comes with HttpResponse, so to avoid this metadata convert Page object to List object
		return page.toList();
	} 
	
	@GetMapping("/expenses/{id}")
	public Expense getExpenseById(@PathVariable Long id) {
		/*	Expense e=null;
		
		try {
			e=service.getExpenseById(id);
		} catch(ResourceNotFoundException ee) {
			System.out.println(ee.getMessage());
		}
		
		return e;	*/
		
		return service.getExpenseById(id);
	}

	@DeleteMapping("/expenses")
	public ResponseEntity<?> deleteExpenseById(@RequestParam Long id) {
		service.deleteExpenseById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// Only save/process client data if meets bean validating conditions, else not to avoid to save null values
	// @Valid - apply with rest controller endpoints arguments/add validations to the REST APIs/validate java bean properties 
	@PostMapping("/expenses")
	public ResponseEntity<Expense> saveExpenseDetails(@Valid @RequestBody Expense expense) {
		Expense savedExpense=service.saveExpenseDetails(expense);
	
		return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
	}
	
	@PutMapping("/expenses/{id}")
	public Expense updateExpenseDetails(@PathVariable Long id, @RequestBody Expense expense) {
		return service.updateExpenseDetails(id, expense);
	}
	
	// {{baseUrl}}/expenses/category?category=Bills&size=2&page=0&sort=id,DESC
	@GetMapping("/expenses/category")
	public List<Expense> getAllExpensesByCategory(@RequestParam String category, Pageable pageable) {
		return service.getAllExpensesByCategory(category, pageable);
	}
	
	// {{baseUrl}}/expenses/name?keyword=Bill&size=2&page=0&sort=id,DESC
	@GetMapping("/expenses/name")
	public List<Expense> getAllExpensesByName(@RequestParam String keyword, Pageable pageable) {
		return service.getAllExpensesByName(keyword, pageable);
	}

	// {{baseUrl}}/expenses/date?startDate=2023-07-06&endDate=2023-08-03&size=2&page=0&sort=id,DESC
	@GetMapping("/expenses/date")
	public List<Expense> getAllExpensesByDateRange(@RequestParam(required=false) Date startDate, 
												@RequestParam(required=false) Date endDate, Pageable pageable) {
		return service.getAllExpensesByDateRange(startDate, endDate, pageable);
	}
	
}
