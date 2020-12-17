/**
 * 
 */
package com.virtue.csr.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="csr_yoy_data")
public class YoYData {
	
	@Field("hs_name")
	private String hsName;
	
	private Integer Jan;
	
	private Integer Feb;
	
	private Integer Mar;
	
	private Integer Apr;
	
	private Integer May;
	
	private Integer Jun;
	
	private Integer Jul;
	
	private Integer Aug;
	
	private Integer Sep;
	
	private Integer Oct;

	private Integer Nov;
	
	private Integer Dec;
	
	private String year;
	
	public String getHsName() {
		return hsName;
	}
	public void setHsName(String hsName) {
		this.hsName = hsName;
	}
	public Integer getJan() {
		return Jan;
	}
	public void setJan(Integer jan) {
		this.Jan = jan;
	}
	public Integer getFeb() {
		return Feb;
	}
	public void setFeb(Integer feb) {
		this.Feb = feb;
	}
	public Integer getMar() {
		return Mar;
	}
	public void setMar(Integer mar) {
		this.Mar = mar;
	}
	public Integer getApr() {
		return Apr;
	}
	public void setApr(Integer apr) {
		this.Apr = apr;
	}
	public Integer getMay() {
		return May;
	}
	public void setMay(Integer may) {
		this.May = may;
	}
	public Integer getJun() {
		return Jun;
	}
	public void setJun(Integer jun) {
		this.Jun = jun;
	}
	public Integer getJul() {
		return Jul;
	}
	public void setJul(Integer jul) {
		this.Jul = jul;
	}
	public Integer getAug() {
		return Aug;
	}
	public void setAug(Integer aug) {
		this.Aug = aug;
	}
	public Integer getSep() {
		return Sep;
	}
	public void setSep(Integer sep) {
		this.Sep = sep;
	}
	public Integer getOct() {
		return Oct;
	}
	public void setOct(Integer oct) {
		this.Oct = oct;
	}
	public Integer getNov() {
		return Nov;
	}
	public void setNov(Integer nov) {
		this.Nov = nov;
	}
	public Integer getDec() {
		return Dec;
	}
	public void setDec(Integer dec) {
		this.Dec = dec;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
