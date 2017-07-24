package com.autolocations.auto_location;

import com.google.gson.annotations.SerializedName;

public class Location {
	// @SerializedName("lat")
	private float lati;
	// @SerializedName("long")
	private float longi;
	private int status;
	private int timestamp;
	private double distanceFromLast;
	private int dow;
	private int status_total;

	@Override
	public String toString() {
		return "Location [id=" + id + ", lati=" + lati + ", longi=" + longi
				+ ", status=" + status + ", timestamp=" + timestamp + "]";
	}

	public Location() {
	}

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getLati() {
		return lati;
	}

	public void setLati(float lati) {
		this.lati = lati;
	}

	public float getLongi() {
		return longi;
	}

	public void setLongi(float longi) {
		this.longi = longi;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public double calcDistance(double lati, double longi)
	{
	
			  double p = 0.017453292519943295;    // Math.PI / 180
			  double a = 0.5 - Math.cos((this.getLati() - lati) * p)/2 + 
			          Math.cos(lati * p) * Math.cos(this.getLati() * p) * 
			          (1 - Math.cos((this.getLongi() - longi) * p))/2;

			  return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
			}

	public double getDistanceFromLast() {
		return distanceFromLast;
	}

	public void setDistanceFromLast(double distanceFromLast) {
		this.distanceFromLast = distanceFromLast;
	}

//	public int getDow() {
//		return dow;
//	}
//
//	public void setDow(int dow) {
//		this.dow = dow;
//	}
//
//	public int getStatus_total() {
//		return status_total;
//	}
//
//	public void setStatus_total(int status_total) {
//		this.status_total = status_total;
//	}
	
	
}
