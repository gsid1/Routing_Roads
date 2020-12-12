/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maps.algorithms.services;

import com.maps.algorithms.model.BaseResponse;
import com.maps.algorithms.model.GeoLocation;
import com.maps.algorithms.model.Location;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author  
 */

public interface LocationService {
        
        public Location showLocationDetailsByName(String name) ;
    	public Location showLocationDetails(String lat,String lng) ;
        public List<Location> findAllLocations() ;
        public Location addLocation(String location) ;
        public Location updateLocation(String location);
	
}
