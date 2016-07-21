package edu.asu.msse.semanticweb.group6.knowyourcity.model;

/**
 * @author Akshay Ashwathanarayana
 *
 */

abstract class Housing {
	private double medianRent = -1;
	private Zipcode isAtZipcode;

	public Zipcode getIsAtZipcode() {
		return isAtZipcode;
	}

	public void setIsAtZipcode(Zipcode isAtZipcode) {
		this.isAtZipcode = isAtZipcode;
	}

	public void setMedianRent(double medianRent) {
		this.medianRent = medianRent;
	}

	public double getMedianRent() {
		return medianRent;
	}

}
