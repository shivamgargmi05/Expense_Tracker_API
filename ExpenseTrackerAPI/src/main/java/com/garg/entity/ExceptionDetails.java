package com.garg.entity;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDetails {

	private Integer httpStatusCodeValue;
	private String exceptionMessage;
	private HttpStatus httpStatusCode;
	private LocalDateTime timestamp;
	
}
