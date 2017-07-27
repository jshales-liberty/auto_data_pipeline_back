package com.autolocations.auto_location;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import io.swagger.annotations.ApiOperation;

@RestController
public class R_Controller {
	@ApiOperation(value = "Test Data Route", notes = "This allows a test data connection (not from the PostGres database, but to a small local JSON file.")
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
	@ApiOperation(value = "Add a driver/vehicle to the database.", notes = "Add a driver and vehicle to the database and they will begin appearing on the map.")
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

	@ApiOperation(value = "Get all users in the database.", notes = "This route probably wont exist in production.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/user", method = RequestMethod.GET)
	public List<AppUser> getUsers() throws URISyntaxException, SQLException {
		return UserDB.getUsers();
	}
	@ApiOperation(value = "Use this to adjust a driver/vehicle.", notes = "Pass a vehicle ID into this route with any characteristics you want to change about that ID.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driver/{vid}", method = RequestMethod.PUT)
	@ResponseBody
	public Driver updateVehAndDriver(
			@PathVariable(name = "vid", required = true) int vid,
			@RequestBody Driver d)
			throws URISyntaxException, SQLException, ClassNotFoundException {
		Driver existing = DriverDB.getDriverInfo(vid);
		existing.merge(d);
		DriverDB.updateDriver(existing);
		return DriverDB.getDriverInfo(vid);
	}
	@ApiOperation(value = "Add a new user.", notes = "Pass a user object to the route to add them to the database.")
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
			throws URISyntaxException, SQLException, ClassNotFoundException {
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
	@RequestMapping(path = "/api/driver/routehistory/{vid}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Double> getCumulativeDistances(
			@PathVariable(name = "vid", required = true) int vid,
			@RequestBody Time t) throws URISyntaxException, SQLException {
		List<Location> locations = LocationDB.getCumulativeDistancesForAll(t);
		double[] result = new double[2];
		Map<String, Double> map = new HashMap<String, Double>();
		double total_distance = 0;
		for (Location l : locations) {
			if (l.getVid() == vid) {
				result[0] = (l.getCumulativeDistance() / t.getDiff());
				map.put("vid_avg", result[0]);
			} else {
				total_distance += l.getCumulativeDistance();
			}
		}

		result[1] = total_distance / (t.getDiff() * locations.size());
		map.put("total_avg", result[1]);
		return map;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/sumbydow/{vid}", method = RequestMethod.POST)
	@ResponseBody
	public List<Summary> getDOWById(
			@PathVariable(name = "vid", required = false) int vid,
			@RequestBody Time t) throws URISyntaxException, SQLException {
		if (vid != 0) {
			return SummaryDB.getDOWbyId(vid, t.getStartTime(), t.getEndTime());
		} else {
			return SummaryDB.getDOWbyId(t.getStartTime(), t.getEndTime());
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/topfiveincidents/", method = RequestMethod.POST)
	@ResponseBody
	public List<Summary> getVehWithMostIncidents(@RequestBody Time t)
			throws URISyntaxException, SQLException {
		return SummaryDB.getVehWithMostIncidents(t.getStartTime(),
				t.getEndTime());
	}
}
