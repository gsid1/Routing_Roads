package com.maps.algorithms.services;

import com.maps.algorithms.dao.LocationDao;
import com.maps.algorithms.model.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImplementation implements LocationService {

    @Autowired
    private LocationDao locationDao ;
    
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

        
	public Location showLocationDetailsByName(String name)
	{
	
                Location location=locationDao.showLocationDetailsByName(name) ;
		return location ;
		
	}

	public Location showLocationDetails(String lat,String lng)
	{
                System.out.println("inside Location Service Implementatiom") ;
                Location location=locationDao.showLocationDetails(lat,lng) ;
//                if(location==null)
//                    return "No data found" ;
		return location ;
		
	}
	public Location addLocation(String location)
	{
            Location newLocation=locationDao.addLocation(location) ;
            return newLocation ;
		
	}
	public Location updateLocation(String location)
	{
	    Location updateLocation=locationDao.updateLocation(location) ;
            return updateLocation ;
	}

    @Override
    public List<Location> findAllLocations() {
        List<Location> locations=locationDao.findAllLocations() ;
        return locations;
    }
}
