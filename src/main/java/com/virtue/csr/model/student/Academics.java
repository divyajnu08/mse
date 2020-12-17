package com.virtue.csr.model.student;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.virtue.csr.constants.SchoolTypes;

public class Academics implements Serializable {
	private static final long serialVersionUID = 6870433445899203901L;
	
	private String studentId;
	private int hsYear;
	private SchoolTypes hsType;
	private Double hsGpa4;
	private int sat;
	private int act;
	private int clt;
	private Double transGpa;
	private Date ts = new Date();
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public int getHsYear() {
		return hsYear;
	}
	public void setHsYear(int hsYear) {
		this.hsYear = hsYear;
	}
	public SchoolTypes getHsType() {
		return hsType;
	}
	public void setHsType(SchoolTypes hsType) {
		this.hsType = hsType;
	}
	public Double getHsGpa4() {
		return hsGpa4;
	}
	public void setHsGpa4(Double hsGpa4) {
		this.hsGpa4 = hsGpa4;
	}
	public int getSat() {
		return sat;
	}
	public void setSat(int sat) {
		this.sat = sat;
	}
	public int getAct() {
		return act;
	}
	public void setAct(int act) {
		this.act = act;
	}
	public int getClt() {
		return clt;
	}
	public void setClt(int clt) {
		this.clt = clt;
	}
	public Double getTransGpa() {
		return transGpa;
	}
	public void setTransGpa(Double transGpa) {
		this.transGpa = transGpa;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}


	public void updateObject(Academics otherObj) {
		this.hsYear = otherObj.hsYear;
		this.hsType = otherObj.hsType;
		this.hsGpa4 = otherObj.hsGpa4;
		this.sat = otherObj.sat;
		this.act = otherObj.act;
		this.clt = otherObj.clt;
		this.transGpa = otherObj.transGpa;
	}
	
	/*
	@Override
	public String toString() {
		return "Academics [studentId=" + studentId + ", highschoolGraduatedYear=" + highschoolGraduatedYear
				+ ", hsType=" + hsType + ", high_school_gpa=" + hsGpa + ", weighted_gpa="
				+ weighted_gpa + ", unweightedGPA=" + unweightedGPA + ", sat_max=" + sat_max + ", satCombined="
				+ satCombined + ", actComposite=" + actComposite + ", AR_state_flag=" + AR_state_flag + ", currentGPA="
				+ currentGPA + "]";
	}
	*/

}
