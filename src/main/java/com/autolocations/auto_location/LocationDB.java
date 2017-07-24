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

	public static Connection getConnection()
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
			double prev_lati;
			double prev_longi;
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
						"Select vid from vehlocation_reserve where timestamp > extract(epoch from now()) Order by timestamp ASC limit 1;");
				PreparedStatement pstmt_2 = conn.prepareStatement(
						"Insert into vehlocation (vid, lati, longi, status, timestamp) "
								+ "select vid, lati, longi, status, timestamp from vehlocation_reserve where vid = ? and timestamp > extract(epoch from now());"
								+ "Delete From vehlocation_reserve where vid = ?;"))

		{
			ResultSet rs = pstmt_1.executeQuery();
			rs.next();
			int newVehID = rs.getInt("vid");
			pstmt_2.setInt(1, newVehID);
			pstmt_2.setInt(2, newVehID);
			pstmt_2.executeUpdate();
			return newVehID;
		}

	}

	public static List<Location> getHistoricalDataByVID(int id, int hop_count)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select t1.Lati, t1.Longi, t1.Status, t1.vid, t1.timestamp "
								+ "from vehlocation t1 where vid=? and timestamp < extract(epoch from now()) "
								+ "ORDER BY timestamp ASC limit ?;");) {
			pstmt.setLong(1, id);
			pstmt.setInt(2, hop_count);
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
	
	public static List<Location> getDOWbyId(int id)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('dow' from to_timestamp(timestamp)) AS dow, sum(status) AS status_total "
						+ "FROM vehlocation where vid = ? "
						+ "GROUP BY 1 ORDER BY 1;");) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			List<Location> dowdata = new ArrayList<Location>();
			while (rs.next()) {
				Location dow = new Location();
				dow.setDow(rs.getInt("dow"));
				dow.setStatus_total(rs.getInt("status_total"));
				dowdata.add(dow);
			}
			return dowdata;
		}

	}
	
	public static List<Location> getHODbyId(int id)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('hour' from to_timestamp(timestamp)) AS hour, sum(status) AS status_total "
						+ "FROM vehlocation where vid = ? "
						+ "GROUP BY 1 ORDER BY 1;");) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			List<Location> hoddata = new ArrayList<Location>();
			while (rs.next()) {
				Location hod = new Location();
				hod.setDow(rs.getInt("hour"));
				hod.setStatus_total(rs.getInt("status_total"));
				hoddata.add(hod);
			}
			return hoddata;
		}

	}
	
	public static void deleteVehLocations(int id) throws URISyntaxException, SQLException {
		try (Connection conn = LocationDB.getConnection();
				PreparedStatement pstmt_1 = conn.prepareStatement(
						"Insert into vehlocation_reserve (vid, lati, longi, status, timestamp) "
								+ "select vid, lati, longi, status, timestamp from vehlocation where vid = ? and timestamp > extract(epoch from now());"
								+ "Delete From vehlocation where vid = ?;"))

		{
			pstmt_1.setInt(1, id);
			pstmt_1.setInt(2, id);
			pstmt_1.executeUpdate();
		}

	}
}