package com.virtue.csr.exception;

@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {
	
	public ApplicationException(String message) {
		super(message);
	}

}