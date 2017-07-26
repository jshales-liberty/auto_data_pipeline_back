package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.autolocations.auto_location.Summary.DayOfWeek;

public class SummaryDB {

	public static Connection getConnection()
			throws URISyntaxException, SQLException {
		String dbUrl = System.getenv("HEROKU_POSTGRESQL_BROWN_JDBC_URL");
		return DriverManager.getConnection(dbUrl);
	}
	
	public static int getMinTimestamp(int id)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select min(timestamp) as mintimestamp "
						+ "from vehlocation "
						+"where vid = ?;");) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt("mintimestamp");
		}
	}
	
	public static List<Summary> getDOWbyId(int id)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('dow' from to_timestamp(timestamp)) AS dow, sum(status) AS status_total "
						+ "FROM vehlocation where vid = ? and timestamp < extract(epoch from now())"
						+ "GROUP BY 1 ORDER BY 1;");) {
			pstmt.setLong(1, id);
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
	
	public static List<Summary> getDOWbyId()
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('dow' from to_timestamp(timestamp)) AS dow, sum(status) AS status_total "
						+ "FROM vehlocation where timestamp < extract(epoch from now())"
						+ "GROUP BY 1 ORDER BY 1;");) {
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
	
	public static List<Summary> getHODbyId(int id)
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('hour' from to_timestamp(timestamp)) AS hour, sum(status) AS status_total "
						+ "FROM vehlocation where vid = ? and timestamp < extract(epoch from now())"
						+ "GROUP BY 1 ORDER BY 1;");) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			List<Summary> hoddata = new ArrayList<Summary>();
			while (rs.next()) {
				Summary hod = new Summary();
				hod.setHour(rs.getInt("hour"));
				hod.setStatus_total(rs.getInt("status_total"));
				hoddata.add(hod);
			}
			return hoddata;
		}

	}
	
	public static List<Summary> getHODbyId()
			throws URISyntaxException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT extract('hour' from to_timestamp(timestamp)) AS hour, sum(status) AS status_total "
						+ "FROM vehlocation where timestamp < extract(epoch from now())"
						+ "GROUP BY 1 ORDER BY 1;");) {
			ResultSet rs = pstmt.executeQuery();
			List<Summary> hoddata = new ArrayList<Summary>();
			while (rs.next()) {
				Summary hod = new Summary();
				hod.setHour(rs.getInt("hour"));
				hod.setStatus_total(rs.getInt("status_total"));
				hoddata.add(hod);
			}
			return hoddata;
		}

	}
}
