package com.api.crudprodutos.config.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> exceptionMethodArgumentNotValid1(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		List<String> messages = new ArrayList<String>();
		fieldErrors.forEach(e -> {
			messages.add(e.getField() + " " + e.getDefaultMessage());
		});
		return new ResponseEntity<>(new ExceptionResponses(HttpStatus.BAD_REQUEST.value(), messages),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> exceptionEmptyResultDataAccess(EmptyResultDataAccessException exception) {
		return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Unable to find product"),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> exceptionEntityNotFound(EntityNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Unable to find product"),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> exceptionHttpMessageNotReadable(HttpMessageNotReadableException exception) {
		return new ResponseEntity<>(
				new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "Required request body is missing"),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> exceptionHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException exception) {
		return new ResponseEntity<>(
				new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), exception.getMessage()),
				HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> exceptionMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
		return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Not Found"),
				HttpStatus.NOT_FOUND);
	}
}