package com.garg.exceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.garg.entity.ExceptionDetails;
import com.garg.exception.ResourceNotFoundException;
import com.garg.exception.UserEmailExistsException;

// To handle HttpStatus Code Related Exceptions, like method argument type mismatch, request body not valid, request not acceptable, etc.
//	GlobalExceptionHandler class must extends ResponseEntityExceptionHandler & override required methods

@RestControllerAdvice
public class AllExceptionsHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException e) {
		ExceptionDetails obj=new ExceptionDetails(
				HttpStatus.NOT_FOUND.value(), e.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now() );
		
		return new ResponseEntity<>(obj, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ExceptionDetails> handle(MethodArgumentTypeMismatchException e) {
		ExceptionDetails obj=new ExceptionDetails(
				HttpStatus.BAD_REQUEST.value(), e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now() );
		
		return new ResponseEntity<>(obj, HttpStatus.BAD_REQUEST);
	}
	
	// It can be handled also like other methods by using MethodArgumentNotValidException name - To Handle @RequestBody Validation Exception
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		
		Map<String, Object> body=new HashMap<>();
		
		body.put("httpStatusCodeValue", HttpStatus.BAD_REQUEST.value() );
		
		// to get customized validation message from exception object
		List<FieldError> lfe=ex.getBindingResult().getFieldErrors();
		Stream<FieldError> sfe=lfe.stream();
		Stream<String> ss=sfe.map( (T) -> T.getDefaultMessage() );
		List<String> ls=ss.collect(Collectors.toList());
		
		body.put("exceptionMessage", ls);
		
		body.put("httpStatusCode", HttpStatus.BAD_REQUEST);
		body.put("timestamp", LocalDateTime.now() );
		
		//	return super.handleMethodArgumentNotValid(ex, headers, status, request);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserEmailExistsException.class)
	public ResponseEntity<ExceptionDetails> handleUserEmailExistsException(UserEmailExistsException e) {
		ExceptionDetails body=new ExceptionDetails(HttpStatus.CONFLICT.value(), e.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now() );
		
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);	// conflict with db email duplicate value with unique constraint integrity violation
	}
	
	// global/generalized exception handler
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionDetails> handleAllOtherExceptions(Exception e) {
		ExceptionDetails obj=new ExceptionDetails(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now() );
		
		return new ResponseEntity<>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
