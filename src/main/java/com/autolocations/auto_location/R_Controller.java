package com.autolocations.auto_location;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@RestController
public class R_Controller {

	@RequestMapping(path = "/api/testdata", method = RequestMethod.GET)
	public Location[] grabdata() {
		Gson gson = new Gson();
		
//		File file = new File(getClass().getClassLoader().getResource("test_data.json").getFile());
//		InputStream in = getClass().getClassLoader().getResourceAsStream("test_data.json");
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader("test_data.json"));
			Location[] data = new Gson().fromJson(reader, Location[].class);
			return data;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
