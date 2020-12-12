/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maps.algorithms.services;

import com.maps.algorithms.dao.ConnectionDao;
import com.maps.algorithms.dao.LocationDao;
import com.maps.algorithms.model.Location;
import static com.maps.algorithms.services.AStarAlgorithm.distance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author  
 */
@Service
public class FloydWarshallAlgorithm {

    @Autowired
    LocationDao locationDao;

    @Autowired
    ConnectionDao connectionDao;

    private Map<String, Map<String, Double>> matrix = null;

    public Map<String, Map<String, Double>> getMatrix() {
        return matrix;
    }

    public Iterator<String> getIterator()
    {
        List<String> list=new ArrayList<>() ;
        for(Map.Entry<String,Map<String,Double>> maps: matrix.entrySet())
        {
            list.add(maps.getKey()) ;
        }
        return (Iterator<String>) list.iterator() ;
    }
    
//    @PostConstruct
    public void floydWarshall() {
        matrix = new HashMap<String, Map<String, Double>>();
        List<Location> allLocation = locationDao.findAllLocations();
        for (Location location : allLocation) {
            System.out.println("for outer for" + location.getName());
            List<Location> neighborLocations = connectionDao.findNeighbors(location.getName());
            double lat1 = location.getGeoLocation().getLatitude();
            double lng1 = location.getGeoLocation().getLongitude();
            Map<String, Double> mapIntermediate = new HashMap<String, Double>();
            mapIntermediate.put(location.getName(), 0.0);
            if(neighborLocations!=null)
            for (Location loc : neighborLocations) {
//                System.out.println("for inner for" + loc.getName());
                double lat2 = loc.getGeoLocation().getLatitude();
                double lng2 = loc.getGeoLocation().getLongitude();
                double dist = distance(lat1, lng1, lat2, lng2, 'M');
                mapIntermediate.put(loc.getName(), dist);
            }
            for (Location loc : allLocation) {
//                System.out.println("for 2nd inner for" + loc.getName());
// OLD 
            	if (mapIntermediate.containsKey(loc.getName())) {
                    continue;
                }
                mapIntermediate.put(loc.getName(), Double.MAX_VALUE);
                
            }
            matrix.put(location.getName(), mapIntermediate);
        }

//OLD
        
//        for (Map.Entry<String, Map<String, Double>> entry : matrix.entrySet()) {
//            String current = entry.getKey();
//            Map<String, Double> intermediateLocation = entry.getValue();
//            for (Map.Entry<String, Double> intermediate : intermediateLocation.entrySet()) {
//                String adjacent = intermediate.getKey();
//                Double dist = intermediate.getValue();
//                for (Location location : allLocation) {
//                    double dist1 = matrix.get(adjacent).get(current);
//                    double dist2 = matrix.get(current).get(location.getName());
//                    double dist3 = matrix.get(adjacent).get(location.getName());
//                    if (dist1 + dist2 < dist3) {
//                        dist3 = dist1 + dist2;
//                        matrix.get(adjacent).put(location.getName(), dist3);
//                    }
//                }
//            }
//        }

// NEW
        for (Location location : allLocation) {
        	double lat1 = location.getGeoLocation().getLatitude();
            double lng1 = location.getGeoLocation().getLongitude();
            for (Location loc : allLocation) {
            	double lat2 = loc.getGeoLocation().getLatitude();
                double lng2 = loc.getGeoLocation().getLongitude();
                double dist = distance(lat1, lng1, lat2, lng2, 'M');
                matrix.get(location.getName()).put(loc.getName(), dist);
            }
        }
        
        for (Location location : allLocation) {
            for (Location loc : allLocation) {
                System.out.println("matrix["+location.getName()+"]["+loc.getName()+"]="+matrix.get(location.getName()).get(loc.getName()));
            }
        }
    }
}
