package com.example.technicaltestinc.enums;

public enum PaymentErrorType {
	DATABASE("database"),
	NETWORK("network"),
	OTHER("other");

	public final String label;

	PaymentErrorType(String label) {
		this.label = label;
	}


}
