package edu.asu.msse.semanticweb.group6.knowyourcity.model;

import java.util.List;

/**
 * @author Akshay Ashwathanarayana
 *
 */

public class City {
	List<Zipcode> zipcodes;

	public List<Zipcode> getZipcodes() {
		return zipcodes;
	}

	public void setZipcodes(List<Zipcode> zipcodes) {
		this.zipcodes = zipcodes;
	}
	
	public void addZipcode(Zipcode zipcode){
		this.zipcodes.add(zipcode);
	}
}
