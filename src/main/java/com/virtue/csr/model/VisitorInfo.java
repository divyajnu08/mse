package com.virtue.csr.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import org.springframework.data.annotation.Id;

@Document(collection = "visitors")
public class VisitorInfo {
	
	@Id
	private String Id;
	private String name;
	private String url;
	@JsonProperty("email")
	private String emailId;
	@JsonProperty("website_name")
	private String websiteName;
	@JsonProperty("ip_address")
	private String ipAddress;
	private String location;
	private Double latitude;
	private Double longitude;
	private Date createdTs;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getWebsiteName() {
		return websiteName;
	}
	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}

	
	public void updateObject(VisitorInfo visitorInfo) {
		this.name=visitorInfo.name;
		this.emailId=visitorInfo.emailId;
		this.ipAddress=visitorInfo.ipAddress;
		this.latitude=visitorInfo.latitude;
		this.longitude=visitorInfo.longitude;
		this.url=visitorInfo.url;
		this.websiteName=visitorInfo.websiteName;
		this.location=visitorInfo.location;
	}
	public Date getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
