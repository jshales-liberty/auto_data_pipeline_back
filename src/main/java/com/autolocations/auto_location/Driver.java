package com.autolocations.auto_location;

public class Driver {
	private int vid;
	private String driver_first_name;
	private String driver_last_name;
	private String vehicle_make;
	private String vehicle_model;
	private int vehicle_year;

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}

	public String getDriver_first_name() {
		return driver_first_name;
	}

	public void setDriver_first_name(String driver_first_name) {
		this.driver_first_name = driver_first_name;
	}

	public String getDriver_last_name() {
		return driver_last_name;
	}

	public void setDriver_last_name(String driver_last_name) {
		this.driver_last_name = driver_last_name;
	}

	public String getVehicle_make() {
		return vehicle_make;
	}

	public void setVehicle_make(String vehicle_make) {
		this.vehicle_make = vehicle_make;
	}

	public String getVehicle_model() {
		return vehicle_model;
	}

	public void setVehicle_model(String vehicle_model) {
		this.vehicle_model = vehicle_model;
	}

	public int getVehicle_year() {
		return vehicle_year;
	}

	public void setVehicle_year(int vehicle_year) {
		this.vehicle_year = vehicle_year;
	}

	public Driver() {
		super();
	}

	public Driver(String driver_first_name, String driver_last_name,
			String vehicle_make, String vehicle_model, int vehicle_year) {
		super();
		this.driver_first_name = driver_first_name;
		this.driver_last_name = driver_last_name;
		this.vehicle_make = vehicle_make;
		this.vehicle_model = vehicle_model;
		this.vehicle_year = vehicle_year;
	}

	public Driver(int vid, String driver_first_name, String driver_last_name,
			String vehicle_make, String vehicle_model, int vehicle_year) {
		super();
		this.vid = vid;
		this.driver_first_name = driver_first_name;
		this.driver_last_name = driver_last_name;
		this.vehicle_make = vehicle_make;
		this.vehicle_model = vehicle_model;
		this.vehicle_year = vehicle_year;
	}

	@Override
	public String toString() {
		return "Driver [vid=" + vid + ", driver_first_name=" + driver_first_name
				+ ", driver_last_name=" + driver_last_name + ", vehicle_make="
				+ vehicle_make + ", vehicle_model=" + vehicle_model
				+ ", vehicle_year=" + vehicle_year + "]";
	}

	// PUT method
	public void merge(Driver other) {
		if (other.driver_first_name != null) {
			this.driver_first_name = other.driver_first_name;
		}
		if (other.driver_last_name != null) {
			this.driver_last_name = other.driver_last_name;
		}
		if (other.vehicle_make != null) {
			this.vehicle_make = other.vehicle_make;
		}
		if (other.vehicle_model != null) {
			this.vehicle_model = other.vehicle_model;
		}
		if (other.vehicle_year != 0) {
			this.vehicle_year = other.vehicle_year;
		}
	}
}
