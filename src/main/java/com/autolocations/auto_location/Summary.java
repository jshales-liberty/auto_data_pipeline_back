package com.autolocations.auto_location;

import com.google.gson.annotations.SerializedName;

public class Summary {
	private int dow;
	private int hour;
	private int status_total;


	public Summary(int dow, int hour, int status_total) {
		this.dow=dow;
		this.hour=hour;
		this.status_total = status_total;
		
	}
	
	public Summary() {
	}

public int getDow() {
	return dow;
}

public void setDow(int dow) {
	this.dow = dow;
}

public int getHour() {
	return hour;
}

public void setHour(int hour) {
	this.hour = hour;
}

public int getStatus_total() {
	return status_total;
}

public void setStatus_total(int status_total) {
	this.status_total = status_total;
}
}