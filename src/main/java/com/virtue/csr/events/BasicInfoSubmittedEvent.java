/**
 * 
 */
package com.virtue.csr.events;

import org.springframework.context.ApplicationEvent;

import com.virtue.csr.model.CSRData;

/**
 *
 */
@SuppressWarnings("serial")
public class BasicInfoSubmittedEvent extends ApplicationEvent{
	
	private CSRData data;
	
	/**
	 * @param source
	 */
	public BasicInfoSubmittedEvent(Object source,CSRData data) {
		super(source);
		this.data=data;
	}

	public CSRData getData() {
		return data;
	}
	
	public void setData(CSRData data) {
		this.data=data;
	}

}
