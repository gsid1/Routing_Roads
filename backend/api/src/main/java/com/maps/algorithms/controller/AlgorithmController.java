package com.maps.algorithms.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.maps.algorithms.model.*;
import com.maps.algorithms.services.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/algorithm")
public class AlgorithmController {
	
	    private static final String SUCCESS_STATUS = "success";
	    private static final String ERROR_STATUS = "error";
	    private static final int CODE_SUCCESS = 100;
	    private static final int AUTH_FAILURE = 102;
            
            @Autowired
            private CalculatePathService calculatePathService ;
            @Autowired
            FloydWarshallAlgorithm floydWarshallAlgorithm ;
            
            @RequestMapping(value="/FloydWarshall",method=RequestMethod.GET) 
	    public void calculateFloydWarshall()
	    {
	    	floydWarshallAlgorithm.floydWarshall();
	    }
            
	    @RequestMapping(value="/AStar",method=RequestMethod.POST) 
	    public List<Location> calculateAStar(@RequestBody String locationEndPoints)
	    {
	        System.out.println("inside Astar controller") ;
	    
                List<Location> list=calculatePathService.calculatePathUsingAStar(locationEndPoints) ;
	    	return list ;
	    }
	    
	    @RequestMapping(value="/Dijkstras",method=RequestMethod.POST) 
	    public List<Location> calculateDijkstras(@RequestBody String locationEndPoints)
	    {
                System.out.println("inside dijkstras controller") ;
	    	List<Location> list=calculatePathService.calculatePathUsingDijkstras(locationEndPoints) ;
	    	return list ;
	    }
	    
	    @RequestMapping(value="/BellmanFord",method=RequestMethod.POST) 
	    public List<Location> calculateBellmanFord(@RequestBody String locationEndPoints)
	    {
	    	List<Location> list=null ;
                System.out.println("inside bellman ford controller") ;    
                try {
                    list = calculatePathService.calculatePathUsingBellmanFord(locationEndPoints);
                } catch (NegativeCycleException ex) {
                    Location defaultLocation=new Location() ;
                    defaultLocation.setName("Negative cycle detected") ;
                    list.add(defaultLocation) ;
                }
	    	return list ;
	    }
}

