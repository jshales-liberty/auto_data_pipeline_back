package com.autolocations.auto_location;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class AppUser {

	private int id;
	private String email;
	private String password_hash;

	public AppUser(String email, String password_hash) {
		this.email = email;
		this.password_hash = password_hash;
}
	
	public AppUser() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
