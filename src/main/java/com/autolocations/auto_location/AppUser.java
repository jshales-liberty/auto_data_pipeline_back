package com.autolocations.auto_location;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class AppUser {

	int id;
	String username;
	String password_hash;

	public AppUser(String username, String password_hash) {
		this.username = username;
		this.password_hash = password_hash;
}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}


	public int getId() {
		return id;
	}

}
