package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
