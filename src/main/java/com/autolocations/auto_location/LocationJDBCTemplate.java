package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LocationJDBCTemplate {

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
