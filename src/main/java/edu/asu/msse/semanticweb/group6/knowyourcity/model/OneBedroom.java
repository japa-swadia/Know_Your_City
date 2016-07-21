package edu.asu.msse.semanticweb.group6.knowyourcity.model;

public class OneBedroom extends Housing {
	

	public OneBedroom(double medianRent, Zipcode zipcode) {
		super();
		this.setMedianRent(medianRent);
		this.setIsAtZipcode(zipcode);
	}

	public OneBedroom() {
		super();
	}
	
	
}
