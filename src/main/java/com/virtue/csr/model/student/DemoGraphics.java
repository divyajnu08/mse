package com.virtue.csr.model.student;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.virtue.csr.constants.AdmissionTypes;
import com.virtue.csr.constants.MaritalStatus;
import com.virtue.csr.constants.Scenario;

@SuppressWarnings("serial")
public class DemoGraphics implements Serializable {

	private String studentId;// userid;
	private String firstName;
	private String middleName;
	private String lastName;
	private int birthYear;
	private int birthMonth;
	private String state;
	private int zip;
	private String country;
	private String academicYear;
	private AdmissionTypes admissionType;
	private MaritalStatus maritalStatus;
	private boolean dependent;;
	private int dependentCount;
	private boolean veteran;
	private boolean spouseVeteran;
	private String studentDependencyStatus;
	private Scenario scenario;
	private boolean usCitizen;
	private boolean parentMilitary;
	private int years_completed;
	private Date ts = new Date();

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public int getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(int birthMonth) {
		this.birthMonth = birthMonth;
	}

	/*
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthate) {
		this.birthDate = birthate;
	}
	*/
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public AdmissionTypes getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(AdmissionTypes admissionType) {
		this.admissionType = admissionType;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public boolean isDependent() {
		return dependent;
	}

	public void setDependent(boolean dependent) {
		this.dependent = dependent;
	}

	public int getDependentCount() {
		return dependentCount;
	}

	public void setDependentCount(int dependentCount) {
		this.dependentCount = dependentCount;
	}

	public boolean isVeteran() {
		return veteran;
	}

	public void setVeteran(boolean veteran) {
		this.veteran = veteran;
	}

	public boolean isSpouseVeteran() {
		return spouseVeteran;
	}

	public void setSpouseVeteran(boolean spouseVeteran) {
		this.spouseVeteran = spouseVeteran;
	}

	public String getStudentDependencyStatus() {
		return studentDependencyStatus;
	}

	public void setStudentDependencyStatus(String studentDependencyStatus) {
		this.studentDependencyStatus = studentDependencyStatus;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public boolean isParentMilitary() {
		return parentMilitary;
	}

	public void setParentMilitary(boolean parentMilitary) {
		this.parentMilitary = parentMilitary;
	}

	public int getYears_completed() {
		return years_completed;
	}

	public void setYears_completed(int years_completed) {
		this.years_completed = years_completed;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	
	public boolean isUsCitizen() {
		return usCitizen;
	}

	public void setUsCitizen(boolean usCitizen) {
		this.usCitizen = usCitizen;
	}
	
	public boolean isInternational() {
		return !usCitizen;
	}
	
	public int getAge() {
		return Calendar.getInstance().get(Calendar.YEAR) - birthYear;
	}
	
	public void updateObject(DemoGraphics otherObj) {
		this.firstName = otherObj.firstName;
		this.middleName = otherObj.middleName;
		this.lastName = otherObj.lastName;
		this.birthYear = otherObj.birthYear;
		this.birthMonth = otherObj.birthMonth;
		this.state = otherObj.state;
		this.zip = otherObj.zip;
		this.country = otherObj.country;
		this.academicYear = otherObj.academicYear;
		this.admissionType = otherObj.admissionType;
		this.maritalStatus = otherObj.maritalStatus;
		this.dependent = otherObj.dependent;
		this.dependentCount = otherObj.dependentCount;
		this.veteran = otherObj.veteran;
		this.spouseVeteran = otherObj.spouseVeteran;
		this.studentDependencyStatus = otherObj.studentDependencyStatus;
		this.scenario = otherObj.scenario;
		this.usCitizen = otherObj.usCitizen;
		this.parentMilitary = otherObj.parentMilitary;
		this.years_completed = otherObj.years_completed;
	}

}
