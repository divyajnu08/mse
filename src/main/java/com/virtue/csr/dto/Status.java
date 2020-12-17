package com.virtue.csr.dto;

import java.io.Serializable;

import com.virtue.csr.constants.CONSTANTS;
import com.virtue.csr.constants.MESSAGE_TYPES;


public class Status implements Serializable{
	private static final long serialVersionUID = 234234523211L;
	private boolean isSuccess = true;
	private String messageType = MESSAGE_TYPES.INFO;
	private String message = CONSTANTS.BLANK;
	private Object data;
	
	public Status() {
		super();
		this.isSuccess = true;
	}
	
	public Status(boolean isSuccess, Object data) {
		super();
		this.isSuccess = isSuccess;
		this.data = data;
	}
	
	public Status(boolean isSuccess, String messageType, String message, Object data) {
		super();
		this.isSuccess = isSuccess;
		this.messageType = messageType;
		this.message = message;
		this.data = data;
	}
	
	public Status(String message, String messageType) {
		super();
		this.isSuccess = false;
		this.messageType = messageType;
		this.message = message;
	}
	
	public String getMessageType() {
		return messageType;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Object getData() {
		return data;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	

}
