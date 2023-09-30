package com.garg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*	Client request to FrontController/DispatcherServlet, each phase o/p is given back to DispatcherServlet then DispatcherServlet 
		connects with next phase and then final response to Client by DispatcherServlet
	Spring MVC components - DispatcherServlet, HandlerMapper, Controller, ModelAndView, ViewResolver, View, BasicErrorController - errorHtml()
	Spring REST components - DispatcherServlet, HandlerMapper, RestController, Model, BasicErrorController - error(), HttpMessageConvertor

1. REST endpoints for Expense Module with Exception Handling & Hibernate validations
2. REST endpoints for User Module
3. Spring Security 
4. Mapping b/w Expense and User 
5. Modified Expense service, repository methods & Modified User rest endpoints, service methods for logged in/authenticated user
6. Jwt Authentication
7. App Deployment to AWS
*/

@SpringBootApplication
public class ExpenseTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApiApplication.class, args);
	}

}
