package com.autolocations.auto_location;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
			@PathVariable(name = "id", required = true) int id, int hop_count)
			throws URISyntaxException, SQLException {
		if (hop_count != 0) {
			return LocationDB.getHistoricalDataByVID(id, hop_count);
		} else {
			return LocationDB.getHistoricalDataByVID(id);
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/addcar", method = RequestMethod.POST)
	@ResponseBody public int addVehAndDriver(@RequestBody Driver d) throws URISyntaxException, SQLException {
		int gen_id = LocationDB.addvehicle();
		d.setVid(gen_id);
		DriverDB.addDriver(d);
		return gen_id;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/addUser", method = RequestMethod.POST)
	@ResponseBody
	public boolean addUser(@RequestBody AppUser u)
			throws URISyntaxException, SQLException {
		return UserDB.adduser(u);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/validateUser", method = RequestMethod.POST)
	@ResponseBody public boolean validateUser(@RequestBody AppUser u) throws URISyntaxException, SQLException {
		return UserDB.validateUser(u);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driverinfo/{id}", method = RequestMethod.GET)
	public Driver getADriver(@PathVariable(name = "id", required = true) int id) throws URISyntaxException, SQLException {
		return DriverDB.getDriverInfo(id);
		
	
	}
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/driverinfo", method = RequestMethod.GET)
public List<Driver> getAllDrivers(@PathVariable(name = "id", required = true) int id) throws URISyntaxException, SQLException {
	return DriverDB.getDriverInfo();}}

