package com.virtue.csr.exception;

@SuppressWarnings("serial")
public class APIException extends RuntimeException{
	
	public APIException(Exception ex) {
		super(ex);
	}
}
