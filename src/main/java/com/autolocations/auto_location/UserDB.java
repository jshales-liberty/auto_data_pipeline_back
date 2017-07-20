package com.autolocations.auto_location;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDB {
	
	private static Connection getConnection()
			throws URISyntaxException, SQLException {
		String dbUrl = System.getenv("HEROKU_POSTGRESQL_BROWN_JDBC_URL");
		return DriverManager.getConnection(dbUrl);
	}
	
public static User addUser()
		throws URISyntaxException, SQLException {
	Connection conn = getConnection();
	PreparedStatement pstmt = conn.prepareStatement(
			"insert into users (username, password_hash) values ('david', crypt('dp', gen_salt('bf')));"
}
}
