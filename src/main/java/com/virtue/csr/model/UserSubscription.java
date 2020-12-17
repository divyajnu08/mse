package com.virtue.csr.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.virtue.csr.constants.Subscription;
import com.virtue.csr.constants.TokenStatus;
import com.virtue.csr.model.stripe.StripeChargeDetails;

import java.time.LocalDateTime;

@Document(collection="user_subscription")
public class UserSubscription {
	
	@Id
	private String id;
	private String token;
	private TokenStatus status;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String emailId;
	private String name;
	private Subscription subscriptionMode;
	private String reason;
	private StripeChargeDetails subscriptionDetails;
	private int refund;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public TokenStatus getStatus() {
		return status;
	}
	public void setStatus(TokenStatus status) {
		this.status = status;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Subscription getSubscriptionMode() {
		return subscriptionMode;
	}
	public void setSubscriptionMode(Subscription subscriptionMode) {
		this.subscriptionMode = subscriptionMode;
	}
	public StripeChargeDetails getSubscriptionDetails() {
		return subscriptionDetails;
	}
	public void setSubscriptionDetails(StripeChargeDetails subscriptionDetails) {
		this.subscriptionDetails = subscriptionDetails;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getRefund() {
		return refund;
	}
	public void setRefund(int refund) {
		this.refund = refund;
	}
}
