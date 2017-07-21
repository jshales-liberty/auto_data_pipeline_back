package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDB {
	public static Driver getDriverInfo(int vid)
			throws URISyntaxException, SQLException {
		try (Connection conn = LocationDB.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select * from driver where vid = ?;")) {
			pstmt.setLong(1, vid);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			Driver driver = new Driver(rs.getInt("vid"),
					rs.getString("driver_first_name"),
					rs.getString("driver_last_name"),
					rs.getString("vehicle_make"), rs.getString("vehicle_model"),
					rs.getInt("vehicle_year"));

			return driver;
		}
	}
	
	public static List<Driver> getDriverInfo()
			throws URISyntaxException, SQLException {
		try (Connection conn = LocationDB.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select * from driver;")) {
			ResultSet rs = pstmt.executeQuery();
			List<Driver> drivers = new ArrayList<Driver>();
			while (rs.next()) {
				Driver driver = new Driver(rs.getInt("vid"),
						rs.getString("driver_first_name"),
						rs.getString("driver_last_name"),
						rs.getString("vehicle_make"), rs.getString("vehicle_model"),
						rs.getInt("vehicle_year"));
				drivers.add(driver);
			}
			return drivers;
		}
	}

	public static void addDriver(Driver d)
			throws URISyntaxException, SQLException {
		try (Connection conn = LocationDB.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Insert into driver("
						+ "vid, driver_first_name, driver_last_name, vehicle_year, vehicle_make, vehicle_model)"
						+ " values (?,?,?,?,?,?);")) {
			pstmt.setLong(1, d.getVid());
			pstmt.setString(2, d.getDriver_first_name());
			pstmt.setString(3, d.getDriver_last_name());
			pstmt.setInt(4, d.getVehicle_year());
			pstmt.setString(5, d.getVehicle_make());
			pstmt.setString(6, d.getVehicle_model());
			pstmt.executeUpdate();
			

		}
	}

	public static void deleteDriver(int vid) throws URISyntaxException, SQLException {
		try (Connection conn = LocationDB.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Delete from driver where vid = ?;")) {
			pstmt.setInt(1, vid);
			pstmt.executeUpdate();
			

		}
	}
}
