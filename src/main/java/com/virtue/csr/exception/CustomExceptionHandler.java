package com.virtue.csr.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.virtue.csr.dto.Status;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{
		private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	    @SuppressWarnings("unchecked")
		@ExceptionHandler(APIException.class)
	    public ResponseEntity<Status> handleAPIException(APIException ex){
	    logger.error("Error in API",ex);
		String stackTrace = Arrays.asList(ex.getStackTrace()).stream().map(o -> o.toString()).limit(200)
				.collect(Collectors.joining("\t"));
	    	return new ResponseEntity(new Status(false,stackTrace), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
}
