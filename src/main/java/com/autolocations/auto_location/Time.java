package com.autolocations.auto_location;

public class Time {
	public Time(int startTime, int endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public Time() {
		super();
	}
	private int startTime;
	private int endTime;
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

}
