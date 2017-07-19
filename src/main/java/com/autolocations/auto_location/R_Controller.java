package com.autolocations.auto_location;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@RestController
public class R_Controller {

	@RequestMapping(path = "/api/testdata", method = RequestMethod.GET)
	public Location[] grabdata() {
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader("test_data.json"));
			Location[] data = new Gson().fromJson(reader, Location[].class);
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}}
	@RequestMapping(path = "/api/testdata2", method = RequestMethod.GET)
	public int grabtest() throws URISyntaxException, SQLException {
		return LocationJDBCTemplate.testpull();

	}

}

