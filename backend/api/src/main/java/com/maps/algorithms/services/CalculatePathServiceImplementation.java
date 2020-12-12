package com.maps.algorithms.services;
import com.maps.algorithms.dao.ConnectionDao;
import com.maps.algorithms.dao.LocationDao;
import com.maps.algorithms.dao.LocationDaoImplementation;
import java.util.List;

import com.maps.algorithms.model.GeoLocation;
import com.maps.algorithms.model.Location;
import com.maps.algorithms.model.LocationEndPoints;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculatePathServiceImplementation implements CalculatePathService {
	
                @Autowired 
                ConnectionDao connectionDao;
                @Autowired
                LocationDao locationDao ;
                @Autowired
                DijkstrasAlgorithm dijkstrasAlgorithm ;
                @Autowired
                AStarAlgorithm aStarAlgorithm ;
                @Autowired
                BellmanFordAlgorithm bellmanFordAlgorithm ;
                
		public List<Location> calculatePathUsingAStar(String locationEndPoints)
		{
                        List<Location> list =null;
                        LocationEndPoints newlocationEndPoint=new LocationEndPoints() ;
                        try { 
                            JSONParser parse = new JSONParser();
                            JSONObject jobj = (JSONObject)parse.parse(locationEndPoints);
                           String str_data1 = (String) jobj.get("source");
                            newlocationEndPoint.setSourceLocation(str_data1);
                            String str_data2 = (String) jobj.get("destination");
                            newlocationEndPoint.setDestinationLocation(str_data2);
                        } catch (ParseException ex) {
                            Logger.getLogger(LocationDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        list=aStarAlgorithm.calculatePath(newlocationEndPoint.getSourceLocation(), newlocationEndPoint.getDestinationLocation());
			return list ;
		}
		public List<Location> calculatePathUsingBellmanFord(String locationEndPoints) throws NegativeCycleException 
		{
			List<Location> list =null;
                        LocationEndPoints newlocationEndPoint=new LocationEndPoints() ;
                        try { 
                            JSONParser parse = new JSONParser();
                            JSONObject jobj = (JSONObject)parse.parse(locationEndPoints);
                           String str_data1 = (String) jobj.get("source");
                            newlocationEndPoint.setSourceLocation(str_data1);
                            String str_data2 = (String) jobj.get("destination");
                            newlocationEndPoint.setDestinationLocation(str_data2);
                        } catch (ParseException ex) {
                            Logger.getLogger(LocationDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        list=bellmanFordAlgorithm.calculatePath(newlocationEndPoint.getSourceLocation(), newlocationEndPoint.getDestinationLocation());
			return list ;
		}
		public List<Location> calculatePathUsingDijkstras(String locationEndPoints) 
		{
                   	List<Location> list =null;
                        LocationEndPoints newlocationEndPoint=new LocationEndPoints() ;
                        try { 
                            JSONParser parse = new JSONParser();
                            JSONObject jobj = (JSONObject)parse.parse(locationEndPoints);
                           String str_data1 = (String) jobj.get("source");
                            newlocationEndPoint.setSourceLocation(str_data1);
                            String str_data2 = (String) jobj.get("destination");
                            newlocationEndPoint.setDestinationLocation(str_data2);
                        } catch (ParseException ex) {
                            Logger.getLogger(LocationDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        list=dijkstrasAlgorithm.calculatePath(newlocationEndPoint.getSourceLocation(), newlocationEndPoint.getDestinationLocation());
			return list ;
		}
		
}
