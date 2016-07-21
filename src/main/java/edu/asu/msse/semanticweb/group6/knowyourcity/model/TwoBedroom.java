package edu.asu.msse.semanticweb.group6.knowyourcity.model;

public class TwoBedroom extends Housing {
	
	public TwoBedroom(double medianRent, Zipcode zipcode) {
		super();
		this.setMedianRent(medianRent);
		this.setIsAtZipcode(zipcode);
	}

	public TwoBedroom() {
		super();
	}
}
