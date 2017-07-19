package com.autolocations.auto_location;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class AppUser {

	int id;
	String username;
	String password;
	String password_hash;
//	String email;

	public AppUser(String username, int id) {
		this.id=id;
		this.username = username;
		
	}
	public AppUser(String username, String password, String email) {
		int id;
		this.username = username;
		this.password = password;
//		this.email = email;
	}

	public AppUser(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
//		this.email = email;
	}

	public AppUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public AppUser(String username) {
		this.username = username;
	}
	
	public void hashPassword() {
		final String SALT = "ELIZABETHDAVID";
		this.hashedPassword = this.password + SALT;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		md.update(this.password.getBytes());
		String digest = new String(md.digest());
		this.hashedPassword=digest;
	}
  
}
