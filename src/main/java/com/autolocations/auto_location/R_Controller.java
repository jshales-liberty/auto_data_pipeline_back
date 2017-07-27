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
	@RequestMapping(path = "/api/routehistory/{vid}/{hop_count}", method = RequestMethod.GET)
	public List<Location> getHistLocs(
			@PathVariable(name = "vid", required = true) int vid,
			@PathVariable(name = "hop_count", required = true) int hop_count)
			throws URISyntaxException, SQLException {
		if (hop_count != 0) {
			return LocationDB.getHistoricalDataByVID(vid, hop_count);
		} else {
			return LocationDB.getHistoricalDataByVID(vid);
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/addDriver", method = RequestMethod.POST)
	@ResponseBody
	public int addVehAndDriver(@RequestBody Driver d)
			throws URISyntaxException, SQLException {
		int gen_id = LocationDB.addvehicle();
		d.setVid(gen_id);
		DriverDB.addDriver(d);
		return gen_id;
	}

	@CrossOrigin(origins = "http://localhost:4200")

	@RequestMapping(path = "/api/user", method = RequestMethod.GET)
	public List<AppUser> getUsers() throws URISyntaxException, SQLException {
		return UserDB.getUsers();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driver/{vid}", method = RequestMethod.PUT)
	@ResponseBody
	public Driver updateVehAndDriver(
			@PathVariable(name = "vid", required = true) int vid,
			@RequestBody Driver d) throws URISyntaxException, SQLException {
		Driver existing = DriverDB.getDriverInfo(vid);
		existing.merge(d);
		DriverDB.updateDriver(existing);
		return DriverDB.getDriverInfo(vid);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/addUser", method = RequestMethod.POST)
	@ResponseBody
	public AppUser addUser(@RequestBody AppUser u)
			throws URISyntaxException, SQLException {
		return UserDB.adduser(u);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/validateUser", method = RequestMethod.POST)
	@ResponseBody
	public AppUser validateUser(@RequestBody AppUser u)
			throws URISyntaxException, SQLException {
		return UserDB.validateUser(u);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driverinfo/{vid}", method = RequestMethod.GET)
	public Driver getADriver(
			@PathVariable(name = "vid", required = true) int vid)
			throws URISyntaxException, SQLException {
		return DriverDB.getDriverInfo(vid);

	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driverinfo", method = RequestMethod.GET)
	public List<Driver> getAllDrivers()
			throws URISyntaxException, SQLException {
		return DriverDB.getDriverInfo();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driver/{vid}", method = RequestMethod.DELETE)
	public void delDriver(@PathVariable(name = "vid", required = true) int vid)
			throws URISyntaxException, SQLException {
		DriverDB.deleteDriver(vid);
		LocationDB.deleteVehLocations(vid);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driver/routehistory", method = RequestMethod.POST)
	@ResponseBody public double getCumulativeDistances(@RequestBody Time t) throws URISyntaxException, SQLException {
		List<Location> locations = LocationDB.getCumulativeDistancesForAll(t);
		double total_distance = 0;
		float daysbetween = t.getDiff();
		locations.size();
		//Location selected = new Location();
		for (Location l : locations) {
			total_distance += l.getCumulativeDistance();
		}
		return total_distance/(daysbetween* locations.size());
	} 

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/sumbydow/{vid}", method = RequestMethod.GET)
public List<Summary> getDOWById(@PathVariable(name = "vid", required = true) int vid) throws URISyntaxException, SQLException {
	if (vid != 0) {
		return SummaryDB.getDOWbyId(vid);
	} else {
		return SummaryDB.getDOWbyId();
	}
}

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/sumbyhour/{vid}", method = RequestMethod.GET)
public List<Summary> getHODById(@PathVariable(name = "vid", required = true) int vid) throws URISyntaxException, SQLException {
	if (vid != 0) {
		return SummaryDB.getHODbyId(vid);
	} else {
		return SummaryDB.getHODbyId();
	}
}
}
