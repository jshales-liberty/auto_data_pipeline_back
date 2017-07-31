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

import com.autolocations.auto_location.Summary.DayOfWeek;

public class SummaryDB {

	public static Connection getConnection()
			throws URISyntaxException, SQLException {
		String dbUrl = System.getenv("HEROKU_POSTGRESQL_BROWN_JDBC_URL");
		return DriverManager.getConnection(dbUrl);
	}
	
//	public static int minTimestamp(int vid)
//			throws URISyntaxException, SQLException {
//		try (Connection conn = getConnection();
//				PreparedStatement pstmt = conn.prepareStatement(
//						"select min(timestamp) as minTimestamp "
//						+ "from vehlocation "
//						+"where vid = ? and timestamp < extract(epoch from now());");) {
//			pstmt.setLong(1, vid);
//			ResultSet rs = pstmt.executeQuery();
//			rs.next();
//			return rs.getInt("minTimestamp");
//		}
//	}
	
//	public static ArrayList<Summary> allTimestamps(int vid)
//			throws URISyntaxException, SQLException {
//		try (Connection conn = getConnection();
//				PreparedStatement pstmt = conn.prepareStatement(
//						"select timestamp as timestamp "
//						+ "from vehlocation "
//						+"where vid = ? and timestamp < extract(epoch from now());");) {
//			pstmt.setLong(1, vid);
//			ResultSet rs = pstmt.executeQuery();
//			ArrayList<Summary> summaries = new ArrayList<Summary>();
//			while (rs.next()) {
//				Summary summary = new Summary();
//				summary.setTimestamp(rs.getInt("timestamp"));
//				summaries.add(summary);
//		} return summaries;
//		}
//	}	
	
	public static List<Summary> getDOWbyId(int vid, int timestampBegin, int timestampEnd)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('dow' from to_timestamp(timestamp)) AS dow, sum(status) AS status_total "
						+ "FROM vehlocation where vid = ? and timestamp < extract(epoch from now()) "
						+ "and timestamp >= ? and timestamp <= ? "
						+ "GROUP BY 1 ORDER BY 1;");) {
			pstmt.setLong(1, vid);
			pstmt.setInt(2, timestampBegin); 
			pstmt.setInt(3, timestampEnd);
			ResultSet rs = pstmt.executeQuery();
			List<Summary> dowdata = new ArrayList<Summary>();
			while (rs.next()) {
				Summary dow = new Summary();
				dow.setDow(DayOfWeek.values()[rs.getInt("dow")]);
				dow.setStatus_total(rs.getInt("status_total"));
				dowdata.add(dow);
			}
			return dowdata;
		}

	}
	
	public static List<Summary> getDOWbyId(int timestampBegin, int timestampEnd)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('dow' from to_timestamp(timestamp)) AS dow, sum(status) AS status_total "
						+ "FROM vehlocation where timestamp < extract(epoch from now())"
						+ "and timestamp >= ? and timestamp <= ? "
						+ "GROUP BY 1 ORDER BY 1;");) {
			pstmt.setInt(1, timestampBegin);
			pstmt.setInt(2, timestampEnd);
			ResultSet rs = pstmt.executeQuery();
			List<Summary> dowdata = new ArrayList<Summary>();
			while (rs.next()) {
				Summary dow = new Summary();
				dow.setDow(DayOfWeek.values()[rs.getInt("dow")]);
				dow.setStatus_total(rs.getInt("status_total"));
				dowdata.add(dow);
			}
			return dowdata;
		}

	}
	
	public static List<Summary> getVehWithMostIncidents(int timestampBegin, int timestampEnd)
	throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select vid as vid, sum(incident) as incidentsum "
						+ "from vehlocation where incident is not null and "
						+ "timestamp < extract(epoch from now()) and "
						+ "timestamp >= ? and timestamp <= ? "
						+ "group by vid order by incidentsum desc limit 5;");) {
			pstmt.setInt(1, timestampBegin);
			pstmt.setInt(2, timestampEnd);
			ResultSet rs = pstmt.executeQuery();
			List<Summary> incidentData = new ArrayList<Summary>();
			while (rs.next()) {
				Summary incident = new Summary();
				incident.setVid(rs.getInt("vid"));
				incident.setIncidentsum(rs.getInt("incidentsum"));
				incidentData.add(incident);
			}
			return incidentData;
		}
	}
	
//	public static List<Summary> getHODbyId(int id)
//			throws URISyntaxException, SQLException {
//		try (Connection conn = getConnection();
//				PreparedStatement pstmt = conn.prepareStatement(
//						"SELECT extract('hour' from to_timestamp(timestamp)) AS hour, sum(status) AS status_total "
//						+ "FROM vehlocation where vid = ? and timestamp < extract(epoch from now())"
//						+ "GROUP BY 1 ORDER BY 1;");) {
//			pstmt.setLong(1, id);
//			ResultSet rs = pstmt.executeQuery();
//			List<Summary> hoddata = new ArrayList<Summary>();
//			while (rs.next()) {
//				Summary hod = new Summary();
//				hod.setHour(rs.getInt("hour"));
//				hod.setStatus_total(rs.getInt("status_total"));
//				hoddata.add(hod);
//			}
//			return hoddata;
//		}
//
//	}
//	
//	public static List<Summary> getHODbyId()
//			throws URISyntaxException, SQLException {
//		try (Connection conn = getConnection();
//				PreparedStatement pstmt = conn.prepareStatement(
//						"SELECT extract('hour' from to_timestamp(timestamp)) AS hour, sum(status) AS status_total "
//						+ "FROM vehlocation where timestamp < extract(epoch from now())"
//						+ "GROUP BY 1 ORDER BY 1;");) {
//			ResultSet rs = pstmt.executeQuery();
//			List<Summary> hoddata = new ArrayList<Summary>();
//			while (rs.next()) {
//				Summary hod = new Summary();
//				hod.setHour(rs.getInt("hour"));
//				hod.setStatus_total(rs.getInt("status_total"));
//				hoddata.add(hod);
//			}
//			return hoddata;
//		}
//
//	}
}
