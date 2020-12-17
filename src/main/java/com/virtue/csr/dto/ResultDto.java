package com.virtue.csr.dto;

public class ResultDto extends KeyValue{
	
	private String label;

	public ResultDto(String key, String value,String label) {
		super(key, value);
		this.label=label;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return "ResultDto [key=" + getKey() + ", value=" + getValue() + ", label=" + label + "]";
	}

}
