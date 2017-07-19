package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDB {

	private static Connection getConnection()
			throws URISyntaxException, SQLException {
		String dbUrl = System.getenv("HEROKU_POSTGRESQL_BROWN_JDBC_URL");
		return DriverManager.getConnection(dbUrl);
	}

	public static int testpull() throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt_validate = conn.prepareStatement(
						"Select count(*) from vehlocation");) {
			ResultSet rs = pstmt_validate.executeQuery();
			rs.next();
			return rs.getInt("count");
		}
	}

	public static List<Location> getCurrentLocations()
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select t1.Lati, t1.Longi, t1.Status, t2.vid, t2.timestamp "
								+ "from vehlocation t1 INNER JOIN (Select vid, max(timestamp) as timestamp "
								+ "from vehlocation where timestamp < extract(epoch from now()) group by vid) t2 "
								+ "ON t1.vid=t2.vid and t1.timestamp=t2.timestamp;");) {
			ResultSet rs = pstmt.executeQuery();
			List<Location> locations = new ArrayList<Location>();
			while (rs.next()) {
				Location location = new Location();
				location.setId(rs.getInt("vid"));
				location.setLati(rs.getFloat("lati"));
				location.setLongi(rs.getFloat("longi"));
				location.setStatus(rs.getInt("status"));
				location.setTimestamp(rs.getInt("timestamp"));
				locations.add(location);
			}
			return locations;
		}

	}

	public static List<Location> getHistoricalDataByVID(int id)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select t1.Lati, t1.Longi, t1.Status, t1.vid, t1.timestamp "
								+ "from vehlocation t1 where vid=? and timestamp < extract(epoch from now()) ORDER BY timestamp ASC;");) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			List<Location> locations = new ArrayList<Location>();
			while (rs.next()) {
				Location location = new Location();
				location.setId(rs.getInt("vid"));
				location.setLati(rs.getFloat("lati"));
				location.setLongi(rs.getFloat("longi"));
				location.setStatus(rs.getInt("status"));
				location.setTimestamp(rs.getInt("timestamp"));
				locations.add(location);
			}
			return locations;
		}

	}

	public static int addvehicle() throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt_1 = conn.prepareStatement(
						"Select vid from Car t1 where timestamp > extract(epoch from now()) Order by timestamp ASC limit 1;");
				PreparedStatement pstmt_2 = conn.prepareStatement(
						"Insert into vehlocation from Car where vid = ? & timestamp > extract(epoch from now();"
								+ "Delete From Car where vid = ?;"))

		{
			ResultSet rs = pstmt_1.executeQuery();
			rs.next();
			int newVehID = rs.getInt("vid");
			pstmt_2.setLong(1, newVehID);
			pstmt_2.executeQuery();
			return newVehID;
		}

	}
}