package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LocationDB {

	public static Connection getConnection()
			throws URISyntaxException, SQLException {
		String dbUrl = System.getenv("HEROKU_POSTGRESQL_BROWN_JDBC_URL");
		if (dbUrl == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dbUrl = "jdbc:postgres://wempudndjjzqhz:5fc6adad0072b1b33c3d0bbfea149b1d94341ca2f6aab2a3a616e10dcbd866bc@ec2-54-163-254-143.compute-1.amazonaws.com:5432/db9bi9pe38ip8j";
		}
		return DriverManager.getConnection(dbUrl);
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
				location.setVid(rs.getInt("vid"));
				location.setLati(rs.getFloat("lati"));
				location.setLongi(rs.getFloat("longi"));
				location.setStatus(rs.getInt("status"));
				location.setTimestamp(rs.getInt("timestamp"));
				locations.add(location);
			}
			return locations;
		}

	}

	public static List<Location> getHistoricalDataByVID(int vid)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select t1.Lati, t1.Longi, t1.Status, t1.vid, t1.timestamp "
								+ "from vehlocation t1 where vid=? and timestamp < extract(epoch from now()) ORDER BY timestamp ASC;");) {
			pstmt.setLong(1, vid);
			ResultSet rs = pstmt.executeQuery();
			double cumulative_distance = 0;
			List<Location> locations = new ArrayList<Location>();
			double prev_lati = 0;
			double prev_longi = 0;
			while (rs.next()) {
				Location location = new Location();
				location.setVid(rs.getInt("vid"));
				location.setLati(rs.getFloat("lati"));
				location.setLongi(rs.getFloat("longi"));
				location.setStatus(rs.getInt("status"));
				location.setTimestamp(rs.getInt("timestamp"));
				if (prev_lati == 0 & prev_longi == 0) {
					location.setDistanceFromLast(0);
				} else {
					location.setDistanceFromLast(
							location.calcDistance(prev_lati, prev_longi));
					cumulative_distance += location.getDistanceFromLast();
				}
				prev_lati = rs.getFloat("lati");
				prev_longi = rs.getFloat("longi");
				location.setCumulativeDistance(cumulative_distance);
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

	public static List<Location> getHistoricalDataByVID(int vid, int hop_count)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select t1.Lati, t1.Longi, t1.Status, t1.vid, t1.timestamp "
								+ "from vehlocation t1 where vid=? and timestamp < extract(epoch from now()) "
								+ "ORDER BY timestamp ASC limit ?;");) {
			pstmt.setLong(1, vid); 
			pstmt.setInt(2, hop_count);
			ResultSet rs = pstmt.executeQuery();
			double cumulative_distance = 0;
			List<Location> locations = new ArrayList<Location>();
			double prev_lati = 0;
			double prev_longi = 0;
			while (rs.next()) {
				Location location = new Location();
				location.setVid(rs.getInt("vid"));
				location.setLati(rs.getFloat("lati"));
				location.setLongi(rs.getFloat("longi"));
				location.setStatus(rs.getInt("status"));
				location.setTimestamp(rs.getInt("timestamp"));
				if (prev_lati == 0 & prev_longi == 0) {
					location.setDistanceFromLast(0);
				} else {
					location.setDistanceFromLast(
							location.calcDistance(prev_lati, prev_longi));
					cumulative_distance += location.getDistanceFromLast();
				}
				prev_lati = rs.getFloat("lati");
				prev_longi = rs.getFloat("longi");
				location.setCumulativeDistance(cumulative_distance);
				locations.add(location);
			}
			return locations;
		}

	}

	public static void deleteVehLocations(int vid)
			throws URISyntaxException, SQLException {
		try (Connection conn = LocationDB.getConnection();
				PreparedStatement pstmt_1 = conn.prepareStatement(
						"Insert into vehlocation_reserve (vid, lati, longi, status, timestamp) "
								+ "select vid, lati, longi, status, timestamp from vehlocation where vid = ? and timestamp > extract(epoch from now());"
								+ "Delete From vehlocation where vid = ?;"
								))

		{
			pstmt_1.setInt(1, vid);
			pstmt_1.setInt(2, vid);
			pstmt_1.setInt(3, vid);
			pstmt_1.executeUpdate();
		}

	}

	public static List<Location> getCumulativeDistancesForAll(Time t)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"Select t1.Lati, t1.Longi, t1.vid "
								+ "from vehlocation t1 where timestamp >= ? and timestamp <= ? "
								+ "ORDER BY vid, timestamp ASC;",
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);) {
			int startTime = t.getStartTime();
			int endTime = t.getEndTime();
			if (startTime != 0) {
				pstmt.setInt(1, startTime);
			} else {
				pstmt.setInt(1, 0);
			}
			if (endTime != 0) {
				pstmt.setInt(2, endTime);
				;
			} else {
				pstmt.setLong(2, Instant.now().getEpochSecond());
				;
			}
			ResultSet rs = pstmt.executeQuery();
			List<Location> locations = new ArrayList<Location>();
			double prev_lati = 0;
			double prev_longi = 0;
			double cumulative_distance = 0;
			rs.next();
			int prev_vid = rs.getInt("vid");
			rs.first();
			Location location = new Location();
			while (rs.next()) {
				if (prev_vid != rs.getInt("vid")) {
					locations.add(location);
					prev_lati = 0;
					prev_longi = 0;
					cumulative_distance = 0;
					prev_vid = rs.getInt("vid");
				}
				location = new Location();
				location.setVid(rs.getInt("vid"));
				location.setLati(rs.getFloat("lati"));
				location.setLongi(rs.getFloat("longi"));
				if (prev_lati == 0 & prev_longi == 0) {
					location.setDistanceFromLast(0);
				} else {
					location.setDistanceFromLast(
							location.calcDistance(prev_lati, prev_longi));
					cumulative_distance += location.getDistanceFromLast();
				}
				prev_lati = rs.getFloat("lati");
				prev_longi = rs.getFloat("longi");
				location.setCumulativeDistance(cumulative_distance);
			}
			locations.add(location);
			return locations;
		}
	}
}