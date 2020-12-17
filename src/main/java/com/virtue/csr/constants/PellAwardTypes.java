package com.virtue.csr.constants;

public enum PellAwardTypes {
	
	FULL_TIME("Annual Full-Time Award"),
	THREEFOURTH_TIME("Annual 3/4 Time Award"),
	HALF_TIME("Annual Half-Time Award"),
	LESSTHANHALF_TIME("annual_lessthan_halftime_award");	

	private final String descr;

    private PellAwardTypes(String descr) {
        this.descr = descr;
    }

    public String toString() {
        return descr;
    }

}
