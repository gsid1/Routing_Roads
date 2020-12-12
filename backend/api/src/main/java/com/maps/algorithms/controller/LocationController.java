package com.maps.algorithms.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.maps.algorithms.model.BaseResponse;
import com.maps.algorithms.model.GeoLocation;
import com.maps.algorithms.model.Location;
import com.maps.algorithms.services.LocationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService ;
    
    @RequestMapping(value="/check",method=RequestMethod.GET) 
    public String defaultCheck()
    {
//        System.out.println("inside location servoce") ;
//    	Location location=locationService.showLocationDetailsByName(name) ;
    	return "Available" ;
    }
    @RequestMapping(value="/{name}",method=RequestMethod.GET) 
    public Location showLocationByName(@PathVariable String name)
    {
        System.out.println("inside location servoce") ;
    	Location location=locationService.showLocationDetailsByName(name) ;
    	return location ;
    }
    
    @RequestMapping(method=RequestMethod.GET) 
    public List<Location> findAllLocations()
    {
        System.out.println("inside location servoce") ;
    	List<Location> locations=locationService.findAllLocations() ;
    	return locations ;
    }
    
    @RequestMapping(value="/lat/{lat}/lng/{lng:.+}",method=RequestMethod.GET) 
    public Location showLocation(@PathVariable String lat,@PathVariable String lng)
    {
        System.out.println("inside location service") ;
    	Location location=locationService.showLocationDetails(lat,lng) ;
    	return location ;
    }
    
    @RequestMapping(value="/add",method=RequestMethod.POST) 
    public Location addLocation(@RequestBody String location)
    {
    	Location newLocation=locationService.addLocation(location) ;
    	return newLocation ;
    }
    
    @RequestMapping(value="/update",method=RequestMethod.PUT) 
    public Location updateLocation(@RequestBody String location)
    {
    	Location updatedLocation=locationService.updateLocation(location) ;
    	return updatedLocation ;
    }	
}
