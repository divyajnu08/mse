package com.virtue.csr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.virtue.csr.dto.ResultDto;
import com.virtue.csr.model.student.Academics;
import com.virtue.csr.model.student.DemoGraphics;
import com.virtue.csr.model.student.Financials;
import com.virtue.csr.model.student.OtherInformation;

@Document(collection="npc")
public class NPCObject implements Serializable {

	private static final long serialVersionUID = 4248115953231910078L;
	
	@Id
	private String id;
	private String studentId;
	private int custid;
	private int campid;
	private Date ts = new Date();
	private Academics academics;
	private OtherInformation others;
	private Financials financials;
	private DemoGraphics demoGraphics;
	private Double parentsContribution;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCustid() {
		return custid;
	}

	public void setCustid(int custid) {
		this.custid = custid;
	}

	public int getCampid() {
		return campid;
	}

	public void setCampid(int campid) {
		this.campid = campid;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Academics getAcademics() {
		return academics;
	}

	public void setAcademics(Academics academics) {
		this.academics = academics;
	}

	public OtherInformation getOthers() {
		return others;
	}

	public void setOthers(OtherInformation others) {
		this.others = others;
	}

	public Financials getFinancials() {
		return financials;
	}

	public void setFinancials(Financials financials) {
		this.financials = financials;
	}

	
	public DemoGraphics getDemoGraphics() {
		return demoGraphics;
	}

	public void setDemoGraphics(DemoGraphics demoGraphics) {
		this.demoGraphics = demoGraphics;
	}
	
	@Override
	public String toString() {
		return "NPCDTODemo [id=" + id + ", academics=" + academics + ", otherInformation=" + others
				+ ", financials=" + financials + ", demoGraphics=" + demoGraphics + "]";
	}

	public Double getParentsContribution() {
		return parentsContribution;
	}

	public void setParentsContribution(Double parentsContribution) {
		this.parentsContribution = parentsContribution;
	}
	

}
