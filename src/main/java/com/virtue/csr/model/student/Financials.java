package com.virtue.csr.model.student;

import java.io.Serializable;
import java.util.Date;

import com.virtue.csr.constants.EmploymentStatus;
import com.virtue.csr.constants.EnrollmentStatus;
import com.virtue.csr.constants.MaritalStatus;

public class Financials implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String studentId;
	private String parentResidencyState;
	private MaritalStatus parentsMritalStatus;
	private int parent1Age;
	private Double parent1Income;
	private int parent2Age;
	private Double parent2Income;
	//Number of college students in parent's household (including parents)
	//Number of college students in independent students household
	private int householdAllStudCount;
	//Number of college students in parents household (excluding parents)
	private int householdStudcountExclParents;
	//Number of people in parent's Household
	//Number in student's household, including student
	private int householdAllCount;
	private Double parentsTotalAssests;
	private Double networthParentsBusiness;
	private Double studentIncome;
	private Double studentTotalAssests;
	private Double studentNetworth;
	private Double familyContribution;
	private EnrollmentStatus spouseEnrollmentStatus;
	private EmploymentStatus employmentStatus;
	private Double spouseIncome;
	private Double parentsTax;
	private Double studentTax;
	private boolean fafsaFiled;
	private Double outsideFunding;
	private Date ts = new Date();
	
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getParentResidencyState() {
		return parentResidencyState;
	}
	public void setParentResidencyState(String parentResidencyState) {
		this.parentResidencyState = parentResidencyState;
	}
	public MaritalStatus getParentsMritalStatus() {
		return parentsMritalStatus;
	}
	public void setParentsMritalStatus(MaritalStatus married) {
		this.parentsMritalStatus = married;
	}
	public int getParent1Age() {
		return parent1Age;
	}
	public void setParent1Age(int parent1Age) {
		this.parent1Age = parent1Age;
	}
	public Double getParent1Income() {
		return parent1Income;
	}
	public void setParent1Income(Double parent1Income) {
		this.parent1Income = parent1Income;
	}
	public int getParent2Age() {
		return parent2Age;
	}
	public void setParent2Age(int parent2Age) {
		this.parent2Age = parent2Age;
	}
	public Double getParent2Income() {
		return parent2Income;
	}
	public void setParent2Income(Double parent2Income) {
		this.parent2Income = parent2Income;
	}

	public Double getParentsTotalAssests() {
		return parentsTotalAssests;
	}
	public void setParentsTotalAssests(Double parentsTotalAssests) {
		this.parentsTotalAssests = parentsTotalAssests;
	}
	public Double getNetworthParentsBusiness() {
		return networthParentsBusiness;
	}
	public void setNetworthParentsBusiness(Double networthParentsBusiness) {
		this.networthParentsBusiness = networthParentsBusiness;
	}
	public Double getStudentIncome() {
		return studentIncome;
	}
	public void setStudentIncome(Double studentIncome) {
		this.studentIncome = studentIncome;
	}
	public Double getStudentTotalAssests() {
		return studentTotalAssests;
	}
	public void setStudentTotalAssests(Double studentTotalAssests) {
		this.studentTotalAssests = studentTotalAssests;
	}
	public Double getStudentNetworth() {
		return studentNetworth;
	}
	public void setStudentNetworth(Double studentNetworth) {
		this.studentNetworth = studentNetworth;
	}
	public Double getFamilyContribution() {
		return familyContribution;
	}
	public void setFamilyContribution(Double familyContribution) {
		this.familyContribution = familyContribution;
	}
	public EnrollmentStatus getSpouseEnrollmentStatus() {
		return spouseEnrollmentStatus;
	}
	public void setSpouseEnrollmentStatus(EnrollmentStatus spouseEnrollmentStatus) {
		this.spouseEnrollmentStatus = spouseEnrollmentStatus;
	}
	public EmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}
	public void setEmploymentStatus(EmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	public Double getSpouseIncome() {
		return spouseIncome;
	}
	public void setSpouseIncome(Double spouseIncome) {
		this.spouseIncome = spouseIncome;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public Double getParentsTax() {
		return parentsTax;
	}
	public void setParentsTax(Double parentsTax) {
		this.parentsTax = parentsTax;
	}
	public Double getStudentTax() {
		return studentTax;
	}
	public void setStudentTax(Double studentTax) {
		this.studentTax = studentTax;
	}
	public boolean isFafsaFiled() {
		return fafsaFiled;
	}
	public void setFafsaFiled(boolean fafsaFiled) {
		this.fafsaFiled = fafsaFiled;
	}
	public Double getOutsideFunding() {
		return outsideFunding;
	}
	public void setOutsideFunding(Double outsideFunding) {
		this.outsideFunding = outsideFunding;
	}
	public int getOldPersonAge() {
		if(this.parentsMritalStatus == MaritalStatus.MARRIED || 
				this.parentsMritalStatus == MaritalStatus.UNMARRIED_LIVING_TOGETHER) {
			if(parent2Age > parent1Age) {
				return parent2Age;
			}
		}		
		return parent1Age;
	}
	
	public int getHouseholdAllStudCount() {
		return householdAllStudCount;
	}
	public void setHouseholdAllStudCount(int householdAllStudCount) {
		this.householdAllStudCount = householdAllStudCount;
	}
	public int getHouseholdStudcountExclParents() {
		return householdStudcountExclParents;
	}
	public void setHouseholdStudcountExclParents(int householdStudcountExclParents) {
		this.householdStudcountExclParents = householdStudcountExclParents;
	}
	public int getHouseholdAllCount() {
		return householdAllCount;
	}
	public void setHouseholdAllCount(int householdAllCount) {
		this.householdAllCount = householdAllCount;
	}
	
	public void updateObject(Financials otherObj) {
		this.parentResidencyState = otherObj.parentResidencyState;
		this.parentsMritalStatus = otherObj.parentsMritalStatus;
		this.parent1Age = otherObj.parent1Age;
		this.parent1Income = otherObj.parent1Income;
		this.parent2Age = otherObj.parent2Age;
		this.parent2Income = otherObj.parent2Income;
		this.householdAllCount = otherObj.householdAllCount;
		this.householdAllStudCount = otherObj.householdAllStudCount;
		this.householdStudcountExclParents = otherObj.householdStudcountExclParents;
		this.parentsTotalAssests = otherObj.parentsTotalAssests;
		this.networthParentsBusiness = otherObj.networthParentsBusiness;
		this.studentIncome = otherObj.studentIncome;
		this.studentTotalAssests = otherObj.studentTotalAssests;
		this.studentNetworth = otherObj.studentNetworth;
		this.familyContribution = otherObj.familyContribution;
		this.spouseEnrollmentStatus = otherObj.spouseEnrollmentStatus;
		this.employmentStatus = otherObj.employmentStatus;
		this.spouseIncome = otherObj.spouseIncome;
		this.parentsTax = otherObj.parentsTax;
		this.studentTax = otherObj.studentTax;
		this.fafsaFiled = otherObj.fafsaFiled;
		this.outsideFunding = otherObj.outsideFunding;
	}
	

}
