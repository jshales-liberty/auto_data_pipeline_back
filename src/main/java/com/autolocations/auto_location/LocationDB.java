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
		Connection conn = getConnection();
		PreparedStatement pstmt_validate = conn
				.prepareStatement("Select count(*) from vehlocation");
		// pstmt_validate.setString(1, u.getUsername());
		// pstmt_validate.setString(2, u.getEmail());
		ResultSet rs = pstmt_validate.executeQuery();
		rs.next();
		return rs.getInt("count");
	}

	public static List<Location> getCurrentLocations()
			throws URISyntaxException, SQLException {
		Connection conn = getConnection();
		PreparedStatement pstmt = conn.prepareStatement(
				"Select t1.Lati, t1.Longi, t1.Status, t2.vid, t2.timestamp "
						+ "from public.'vehlocation' t1 INNER JOIN (Select vid, max(timestamp) as timestamp "
						+ "from public.'vehlocation' where timestamp < extract(epoch from now()) group by vid) t2 "
						+ "ON t1.vid=t2.vid and t1.timestamp=t2.timestamp;");
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

// public Student getStudent(Integer id) {
// String SQL = "select * from Student where id = ?";
// Student student = jdbcTemplateObject.queryForObject(SQL,
// new Object[]{id}, new StudentMapper());
//
// return student;
// }
// public List<Student> listStudents() {
// String SQL = "select * from Student";
// List <Student> students = jdbcTemplateObject.query(SQL, new StudentMapper());
// return students;
// }
// public void delete(Integer id) {
// String SQL = "delete from Student where id = ?";
// jdbcTemplateObject.update(SQL, id);
// System.out.println("Deleted Record with ID = " + id );
// return;
// }
// public void update(Integer id, Integer age){
// String SQL = "update Student set age = ? where id = ?";
// jdbcTemplateObject.update(SQL, age, id);
// System.out.println("Updated Record with ID = " + id );
// return;
