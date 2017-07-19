package com.autolocations.auto_location;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LocationMapper implements RowMapper<Location> {
   public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
      Location location = new Location();
      location.setId(rs.getInt("vid"));
      location.setLati(rs.getFloat("lati"));
      location.setLongi(rs.getFloat("longi"));
      location.setStatus(rs.getInt("status"));
      location.setTimestamp(rs.getInt("timestamp"));
      
      return location;
   }
}
