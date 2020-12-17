package com.virtue.csr.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResultObject {

	private Double efc;
	private Double coa; // sum of fee items
	private Double originalNeed;
	private Double pellGrant;
	private Double fseog;
	private List<FeesObject> feesItems;
	private List<MeritObject> meritGrant;
	private Double merit;
	private Double collegeSpecific;
	private Double stateGrant;
	private Double dsl;
	private Double dusl;
	private Double privateFunding;
	private Double remainingNeed;
	private Double fws;
	private Double otherLoans;
	private Double netPrice;
	private Double totalGrants;
	private Double needAid;
	private Double addtnFinancialAid;
	private Double monthlyPayment;
	private Map<String,Double> otherAwards;
	
	public Double getTotalGrants() {
		return totalGrants;
	}

	public void setTotalGrants(Double totalGrants) {
		this.totalGrants = totalGrants;
	}
	
	public Double getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(Double netPrice) {
		this.netPrice = netPrice;
	}

	public Double getOtherLoans() {
		return otherLoans;
	}

	public void setOtherLoans(Double otherLoans) {
		this.otherLoans = otherLoans;
	}

	public Double getFws() {
		return fws;
	}

	public void setFws(Double fws) {
		this.fws = fws;
	}

	public Double getEfc() {
		return efc;
	}

	public void setEfc(Double efc) {
		this.efc = efc;
	}

	public void setStateGrant(Double stateGrant) {
		this.stateGrant = stateGrant;
	}

	public Double getCoa() {
		return coa;
	}

	public void setCoa(Double coa) {
		this.coa = coa;
	}

	public Double getOriginalNeed() {
		return originalNeed;
	}

	public void setOriginalNeed(Double originalNeed) {
		this.originalNeed = originalNeed;
	}

	public Double getPellGrant() {
		return pellGrant;
	}

	public void setPellGrant(Double pellGrant) {
		this.pellGrant = pellGrant;
	}

	public Double getFseog() {
		return fseog;
	}

	public void setFseog(Double fseog) {
		this.fseog = fseog;
	}

	public List<FeesObject> getFeesItems() {
		return feesItems;
	}

	public void setFeesItems(List<FeesObject> feesItems) {
		this.feesItems = feesItems;
	}

	public List<MeritObject> getMeritGrant() {
		return meritGrant;
	}

	public void setMeritGrant(List<MeritObject> meritGrant) {
		this.meritGrant = meritGrant;
	}
	
	@JsonIgnore
	public Double getNetCost() { 
		Double grants = stateGrant + pellGrant + merit;
		return coa - grants;
	}
	
	public Double getStateGrant() {
		return stateGrant; 
	}

	public Double getMerit() {
		return merit;
	}

	public void setMerit(Double merit) {
		this.merit = merit;
	}

	public Double getCollegeSpecific() {
		return collegeSpecific;
	}

	public void setCollegeSpecific(Double collegeSpecific) {
		this.collegeSpecific = collegeSpecific;
	}

	public Double getDsl() {
		return dsl;
	}

	public void setDsl(Double dsl) {
		this.dsl = dsl;
	}

	public Double getDusl() {
		return dusl;
	}

	public void setDusl(Double dusl) {
		this.dusl = dusl;
	}

	public Double getPrivateFunding() {
		return privateFunding;
	}

	public void setPrivateFunding(Double privateFunding) {
		this.privateFunding = privateFunding;
	}

	public Double getRemainingNeed() {
		return remainingNeed;
	}

	public void setRemainingNeed(Double remainingNeed) {
		this.remainingNeed = remainingNeed;
	}

	public Double getNeedAid() {
		return needAid;
	}

	public void setNeedAid(Double needAid) {
		this.needAid = needAid;
	}

	public Double getAddtnFinancialAid() {
		return addtnFinancialAid;
	}

	public void setAddtnFinancialAid(Double addtnFinancialAid) {
		this.addtnFinancialAid = addtnFinancialAid;
	}

	public Double getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(Double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public Map<String,Double> getOtherAwards() {
		return otherAwards;
	}

	public void setOtherAwards(Map<String,Double> otherAwards) {
		this.otherAwards = otherAwards;
	}
}
