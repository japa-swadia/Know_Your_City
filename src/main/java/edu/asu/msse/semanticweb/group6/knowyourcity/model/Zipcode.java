package edu.asu.msse.semanticweb.group6.knowyourcity.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akshay Ashwathanarayana
 *
 */

public class Zipcode {
	private int zipcode;
	private double crimeRisk = -1;
	private double earthquakeRisk = -1;
	private double airPollutionIndex = -1;
	private double medianTime = -1;
	private Housing oneBedroom;
	private Housing twoBedroom;
	private Housing condo;
	List<Walmart> walmarts;
	
	
	public Zipcode() {
		super();
		walmarts = new ArrayList<Walmart>();
	}

	public List<Walmart> getWalmarts() {
		return walmarts;
	}

	public void setWalmarts(List<Walmart> walmarts) {
		this.walmarts = walmarts;
	}

	public Housing getOneBedroom() {
		return oneBedroom;
	}

	public void setOneBedroom(Housing oneBedroom) {
		this.oneBedroom = oneBedroom;
	}

	public Housing getTwoBedroom() {
		return twoBedroom;
	}

	public void setTwoBedroom(Housing twoBedroom) {
		this.twoBedroom = twoBedroom;
	}

	public Housing getCondo() {
		return condo;
	}

	public void setCondo(Housing condo) {
		this.condo = condo;
	}

	public double getMedianTime() {
		return medianTime;
	}

	public void setMedianTime(double medianTime) {
		this.medianTime = medianTime;
	}

	public double getCrimeRisk() {
		return crimeRisk;
	}

	public void setCrimeRisk(double crimeRisk) {
		this.crimeRisk = crimeRisk;
	}

	public double getEarthquakeRisk() {
		return earthquakeRisk;
	}

	public void setEarthquakeRisk(double earthquakeRisk) {
		this.earthquakeRisk = earthquakeRisk;
	}

	public double getAirPollutionIndex() {
		return airPollutionIndex;
	}

	public void setAirPollutionIndex(double airPollutionIndex) {
		this.airPollutionIndex = airPollutionIndex;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

}
