package com.maps.algorithms.dao;

import com.maps.algorithms.model.GeoLocation;
import com.maps.algorithms.model.Location;
import java.util.List;

public interface LocationDao {

        public Location showLocationDetailsByName(String name) ;
	public Location showLocationDetails(String lat,String lng);
	public List<Location> findAllLocations();	
        public Location addLocation(String location);
	public Location updateLocation(String location);
}
