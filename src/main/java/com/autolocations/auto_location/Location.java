package com.autolocations.auto_location;

//import com.google.gson.annotations.SerializedName;

public class Location {
	// @SerializedName("lat")
	private float lati;
	// @SerializedName("long")
	private float longi;
	private int status;
	private int timestamp;
	private double distanceFromLast;
	private double cumulativeDistance;

	@Override
	public String toString() {
		return "Location [vid=" + vid + ", lati=" + lati + ", longi=" + longi
				+ ", status=" + status + ", timestamp=" + timestamp + "]";
	}

	public Location() {
	}

	// @SerializedName("id")
	private int vid;

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
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

	public double calcDistance(double lati, double longi) {

		double p = 0.017453292519943295; // Math.PI / 180
		double a = 0.5 - Math.cos((this.getLati() - lati) * p) / 2
				+ Math.cos(lati * p) * Math.cos(this.getLati() * p)
						* (1 - Math.cos((this.getLongi() - longi) * p)) / 2;

		return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
	}

	public double getDistanceFromLast() {
		return distanceFromLast;
	}

	public void setDistanceFromLast(double distanceFromLast) {
		this.distanceFromLast = distanceFromLast;
	}

	public double getCumulativeDistance() {
		return cumulativeDistance;
	}

	public void setCumulativeDistance(double cumulativeDistance) {
		this.cumulativeDistance = cumulativeDistance;
	}

}
