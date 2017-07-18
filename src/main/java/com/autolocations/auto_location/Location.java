package com.autolocations.auto_location;

import com.google.gson.annotations.SerializedName;

public class Location {
	@SerializedName("lat")
	private float lati;
	@SerializedName("long")
	private float longi;
	private int status;
	private int timestamp;

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

}
