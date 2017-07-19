package com.autolocations.auto_location;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@RestController
public class R_Controller {
	@CrossOrigin(origins = "http://localhost:4200")
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
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/testdata2", method = RequestMethod.GET)
	public int grabtest() throws URISyntaxException, SQLException {
		return LocationDB.testpull();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/updatelocations", method = RequestMethod.GET)
	public List<Location> getLocs() throws URISyntaxException, SQLException {
		return LocationDB.getCurrentLocations();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/routehistory/{id}", method = RequestMethod.GET)
	public List<Location> getHistLocs(
			@PathVariable(name = "id", required = true) int id)
			throws URISyntaxException, SQLException {
		return LocationDB.getHistoricalDataByVID(id);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/addcar", method = RequestMethod.GET)
	public List<Location> addrandcarr(
			@PathVariable(name = "id", required = true) int id)
			throws URISyntaxException, SQLException {
		return LocationDB.getHistoricalDataByVID(id);
	}
}
