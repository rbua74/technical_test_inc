package com.example.technicaltestinc.enums;

public enum PaymentType {
	ONLINE("online"),
	OFFLINE("offline");

	public final String label;

	PaymentType(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return this.name();
	}

}
