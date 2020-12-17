package com.virtue.csr.model;

import com.virtue.csr.constants.PaymentOptions;

public class PaymentInfo {
	
	Double netPriceAdvAmount;
	Double fafsaAdAmount;
	Double loanAdvAmount;
	Double installment;
	
	public Double getNetPriceAdvAmount() {
		return netPriceAdvAmount;
	}
	public void setNetPriceAdvAmount(Double netPriceAdvAmount) {
		this.netPriceAdvAmount = netPriceAdvAmount;
	}
	public Double getFafsaAdAmount() {
		return fafsaAdAmount;
	}
	public void setFafsaAdAmount(Double fafsaAdAmount) {
		this.fafsaAdAmount = fafsaAdAmount;
	}
	public Double getLoanAdvAmount() {
		return loanAdvAmount;
	}
	public void setLoanAdvAmount(Double loanAdvAmount) {
		this.loanAdvAmount = loanAdvAmount;
	}
	public Double getInstallment() {
		return installment;
	}
	public void setInstallment(Double installment) {
		this.installment = installment;
	}
}
