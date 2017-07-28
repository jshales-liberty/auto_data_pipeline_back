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

	@ApiOperation(value = "Get newest locations", notes = "Returns each vehicle's most recently reported location.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/updatelocations", method = RequestMethod.GET)
	public List<Location> getLocs() throws URISyntaxException, SQLException {
		return LocationDB.getCurrentLocations();
	}

	@ApiOperation(value = "Get historical data for a car.", notes = "Pass an id to get a list of all locations previously traveled by a vehicle for a given number of 'hops'. "
			+ "Results are sorted from oldest to newest, with newest always being the present timestamp.")
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

	@ApiOperation(value = "Validate a user.", notes = "This route confirms a user has entered the correct username and password, "
			+ "and if so logs them in allowing access to the rest of the site.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/validateUser", method = RequestMethod.POST)
	@ResponseBody
	public AppUser validateUser(@RequestBody AppUser u)
			throws URISyntaxException, SQLException {
		return UserDB.validateUser(u);
	}

	@ApiOperation(value = "Get driver/vehicle info by id.", notes = "Pass an id to get the first name, last name, vehicle year, make and model for the driver/vehicle.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driverinfo/{vid}", method = RequestMethod.GET)
	public Driver getADriver(
			@PathVariable(name = "vid", required = true) int vid)
			throws URISyntaxException, SQLException, ClassNotFoundException {
		return DriverDB.getDriverInfo(vid);

	}

	@ApiOperation(value = "Get driver/vehicle info for all ids.", notes = "This returns the first name, last name, vehicle year, make and model for all drivers/vehicles.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driverinfo", method = RequestMethod.GET)
	public List<Driver> getAllDrivers()
			throws URISyntaxException, SQLException {
		return DriverDB.getDriverInfo();
	}

	@ApiOperation(value = "Delete a vehicle/driver by id.", notes = "Pass an id to remove the vehicle/driver from the database, and all of their historical locations.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driver/{vid}", method = RequestMethod.DELETE)
	public void delDriver(@PathVariable(name = "vid", required = true) int vid)
			throws URISyntaxException, SQLException {
		DriverDB.deleteDriver(vid);
		LocationDB.deleteVehLocations(vid);
	}

	@ApiOperation(value = "Average miles traveled per day", notes = "Given a time range, averages the miles per day traveled by all cars in the dataset. "
			+ "By providing a vehicle ID, this will average the given car's miles per day for the range compared to the average per day for "
			+ "the rest of the dataset.")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(path = "/api/driver/routehistory/{vid}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Double> getCumulativeDistances(
			@PathVariable(name = "vid", required = true) int vid,
			@RequestBody Time t) throws URISyntaxException, SQLException {
		List<Location> locations = LocationDB.getCumulativeDistancesForAll(t);
		Map<String, Double> map = new HashMap<String, Double>();
		double total_distance = 0;
		for (Location l : locations) {
			if (l.getVid() == vid) {
				map.put("vid_avg", (l.getCumulativeDistance() / t.getDiff()));
			} else {
				total_distance += l.getCumulativeDistance();
			}
		}

		map.put("total_avg", total_distance / (t.getDiff() * locations.size()));
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
