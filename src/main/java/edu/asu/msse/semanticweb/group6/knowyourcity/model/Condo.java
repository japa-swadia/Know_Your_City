package edu.asu.msse.semanticweb.group6.knowyourcity.model;

public class Condo extends Housing {
	
	public Condo(double medianRent, Zipcode zipcode) {
		super();
		this.setMedianRent(medianRent);
		this.setIsAtZipcode(zipcode);
	}
	
	public Condo() {
		super();
	}
}

