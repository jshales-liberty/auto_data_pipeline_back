package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.autolocations.auto_location.Summary.DayOfWeek;
import com.google.gson.annotations.SerializedName;

public class Summary {
	private DayOfWeek dow;
	private int hour;
	private int status_total;
	private int timestamp;


	public Summary(DayOfWeek dow, int hour, int status_total) {
		this.dow=dow;
		this.hour=hour;
		this.status_total = status_total;
		this.timestamp = timestamp;
		
	}
	
	public enum DayOfWeek {
	    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
	}
	
	public Summary() {
	}

public DayOfWeek getDow() {
	return dow;
}

public void setDow(DayOfWeek dayOfWeek) {
	this.dow = dayOfWeek;
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

public int getTimestamp() {
	return timestamp;
}

public void setTimestamp(int timestamp) {
	this.timestamp = timestamp;
}

}