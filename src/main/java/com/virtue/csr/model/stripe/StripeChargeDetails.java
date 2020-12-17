package com.virtue.csr.model.stripe;

public class StripeChargeDetails  {
	
	private String id;
	private Long amount;
	private String balanceTransaction;
	private String calculatedStatementDescriptor;
	private String currency;
	private String description;
	private Boolean paid;
	private String receiptUrl;
	private String status;
	private String customerId;
	private String cardTokenId;
	private String priceId;
	private String subscriptionId;
	private String plan;
	private String interval;
    private int intervalCount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getBalanceTransaction() {
		return balanceTransaction;
	}
	public void setBalanceTransaction(String balanceTransaction) {
		this.balanceTransaction = balanceTransaction;
	}
	public String getCalculatedStatementDescriptor() {
		return calculatedStatementDescriptor;
	}
	public void setCalculatedStatementDescriptor(String calculatedStatementDescriptor) {
		this.calculatedStatementDescriptor = calculatedStatementDescriptor;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	public String getReceiptUrl() {
		return receiptUrl;
	}
	public void setReceiptUrl(String receiptUrl) {
		this.receiptUrl = receiptUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCardTokenId() {
		return cardTokenId;
	}
	public void setCardTokenId(String cardTokenId) {
		this.cardTokenId = cardTokenId;
	}
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public int getIntervalCount() {
		return intervalCount;
	}
	public void setIntervalCount(int intervalCount) {
		this.intervalCount = intervalCount;
	}
}
