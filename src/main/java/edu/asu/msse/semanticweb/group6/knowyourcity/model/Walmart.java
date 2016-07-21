package edu.asu.msse.semanticweb.group6.knowyourcity.model;

public class Walmart {

	private Zipcode isAtZipcode;
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Zipcode getIsAtZipcode() {
		return isAtZipcode;
	}

	public void setIsAtZipcode(Zipcode isAtZipcode) {
		this.isAtZipcode = isAtZipcode;
	}
}
