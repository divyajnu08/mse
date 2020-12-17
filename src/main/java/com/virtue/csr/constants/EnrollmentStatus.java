package com.virtue.csr.constants;

public enum EnrollmentStatus {
	
	LESS_THAN_HALF("less than 1/2"),
	MORE_THAN_HALF("more than 1/2");
	
	private final String descr;

    private EnrollmentStatus(String descr) {
        this.descr = descr;
    }

    public String toString() {
        return descr;
    }
}
