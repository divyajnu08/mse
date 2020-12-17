package com.virtue.csr.customer;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="customers")
public class Customer {
	
	private long custid;
	private String name;
	private String address;
	private String contactNumber;
	private String stateCode;
	private long netPrice;
	private long coa;
	private long graduation_rate;
	private long inStateTutionFee;
	private long outStateTutionFee;
	private boolean active;
	private boolean housing;
	private String opeid;
	private String applyURL;
	private Date ts;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public long getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(long netPrice) {
		this.netPrice = netPrice;
	}
	public long getInStateTutionFee() {
		return inStateTutionFee;
	}
	public void setInStateTutionFee(long inStateTutionFee) {
		this.inStateTutionFee = inStateTutionFee;
	}
	public long getOutStateTutionFee() {
		return outStateTutionFee;
	}
	public void setOutStateTutionFee(long outStateTutionFee) {
		this.outStateTutionFee = outStateTutionFee;
	}
	public boolean isHousing() {
		return housing;
	}
	public void setHousing(boolean housing) {
		this.housing = housing;
	}
	public String getOpeid() {
		return opeid;
	}
	public void setOpeid(String opeid) {
		this.opeid = opeid;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public long getCustid() {
		return custid;
	}
	public void setCustid(long custid) {
		this.custid = custid;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getApplyURL() {
		return applyURL;
	}
	public void setApplyURL(String applyURL) {
		this.applyURL = applyURL;
	}
	public long getCoa() {
		return coa;
	}
	public void setCoa(long coa) {
		this.coa = coa;
	}
	public long getGraduation_rate() {
		return graduation_rate;
	}
	public void setGraduation_rate(long graduation_rate) {
		this.graduation_rate = graduation_rate;
	}

	
}