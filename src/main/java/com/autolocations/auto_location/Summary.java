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
	private int timestampBegin;
	private int timestampEnd;
	private int vid;
	private int incidentsum;


	public Summary(DayOfWeek dow, int hour, int status_total) {
		this.dow=dow;
		this.hour=hour;
		this.status_total = status_total;
		this.timestamp = timestamp;
		this.timestampBegin = timestampBegin;
		this.timestampEnd = timestampEnd;
		this.vid = vid;
		this.incidentsum = incidentsum;
		
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

public int getTimestampBegin() {
	return timestampBegin;
}

public void setTimestampBegin(int timestampBegin) {
	this.timestampBegin = timestampBegin;
}

public int getTimestampEnd() {
	return timestampEnd;
}

public void setTimestampEnd(int timestampEnd) {
	this.timestampEnd = timestampEnd;
}

public int getVid() {
	return vid;
}

public void setVid(int vid) {
	this.vid = vid;
}

public int getIncidentsum() {
	return incidentsum;
}

public void setIncidentsum(int incidentsum) {
	this.incidentsum = incidentsum;
}

}