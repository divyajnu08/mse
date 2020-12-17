package com.virtue.csr.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="csr_config")
public class MasterConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String groupKey;
	private String key;
	private String value;
	@JsonIgnore
	private int custid;
	@JsonIgnore
	private int campid;
	@JsonIgnore
	private Date ts;


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
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

	public void setCampid(int campId) {
		this.campid = campId;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "NpcMasterConfig [groupKey=" + groupKey + ", key=" + key + ", value=" + value + "]";
	}

}
