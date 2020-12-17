package com.virtue.csr.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.util.ObjectUtils;

import com.virtue.csr.dto.KeyValue;

public class ScholarshipInfo {
	
	@Id
	private long id;
	private String name;
	private double coa;
	private Double grants;
	private Double otherGrants;
	private List<KeyValue> grants_breakdown;
	private List<KeyValue> otherGrants_breakdown;
	private Date deadline;
	private String applyURL;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getGrants() {
		return grants;
	}
	public void setGrants(Double grants) {
		this.grants = grants;
	}
	public List<KeyValue> getGrants_breakdown() {
		return grants_breakdown;
	}
	public void setGrants_breakdown(List<KeyValue> grantsBreakdown) {
		this.grants_breakdown = grantsBreakdown;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public String getApplyURL() {
		return applyURL;
	}
	public void setApplyURL(String applyURL) {
		this.applyURL = applyURL;
	}
	
	public Double getOtherGrants() {
		return otherGrants;
	}
	public void setOtherGrants(Double otherGrants) {
		this.otherGrants = otherGrants;
	}
	public List<KeyValue> getOtherGrants_breakdown() {
		return otherGrants_breakdown;
	}
	public void setOtherGrants_breakdown(List<KeyValue> otherGrants_breakdown) {
		this.otherGrants_breakdown = otherGrants_breakdown;
	}
	
	public double getCoa() {
		return coa;
	}
	public void setCoa(double coa) {
		this.coa = coa;
	}
	
	
	@Override
    public boolean equals(Object o) {
		if(o instanceof ScholarshipInfo) {
			ScholarshipInfo obj=(ScholarshipInfo)o;
			return ObjectUtils.nullSafeEquals(this.getName(), obj.getName()) 
					&& ObjectUtils.nullSafeEquals(this.getCoa(), obj.getCoa())
					&& ObjectUtils.nullSafeEquals(this.getGrants(), obj.getGrants())
					&& ObjectUtils.nullSafeEquals(this.getOtherGrants(), obj.getOtherGrants())
					&& ObjectUtils.nullSafeEquals(this.getDeadline(), obj.getDeadline())
					&& ObjectUtils.nullSafeEquals(this.getApplyURL(), obj.getApplyURL())
					&& ObjectUtils.nullSafeEquals(this.getGrants_breakdown(),obj.getGrants_breakdown())
					&& ObjectUtils.nullSafeEquals(this.getOtherGrants_breakdown(),obj.getOtherGrants_breakdown());
		}
		return false;
	}
	
	
	

}
