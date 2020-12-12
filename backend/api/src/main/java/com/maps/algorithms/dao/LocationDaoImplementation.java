/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maps.algorithms.dao;

import com.google.gson.Gson;
//import org.json.simple.parser.JSONParser;
import com.maps.algorithms.model.GeoLocation;
import com.maps.algorithms.model.Location;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Sphere;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 *
 * @author  
 */
@Repository("locationDao")
public class LocationDaoImplementation implements LocationDao {

    @Autowired
    private MongoOperations mongoOps;
    private static final String LOCATION_COLLECTION = "Locations";

    @Override
    public Location showLocationDetailsByName(String name) {
        Query searchUserQuery = new Query(Criteria.where("name").is(name));

        Location location = null;
        location = mongoOps.findOne(searchUserQuery, Location.class);
//        GeoLocation geoLocation= mongoOps.findOne(searchUserQuery, GeoLocation.class) ;
//        location.setGeoLocation(geoLocation);
//        System.out.println(geoLocation) ;

        System.out.println();
        System.out.println();
        if(location!=null)
        System.out.println(location.getName()+" Location returned....");
        return location;
    }

    @Override
    public Location showLocationDetails(String lat, String lng) {
        System.out.println("inside showLocationDetails of dao");
        Query searchUserQuery = new Query(Criteria.where("geolocation.lat").is(lat).andOperator(Criteria.where("geolocation.lng").is(lng)));
//        Query searchUserQuery2 = new Query() ;
        Location location = mongoOps.findOne(searchUserQuery, Location.class);
//        Location location2= mongoOps.findOne(searchUserQuery, Location.class) ;

        System.out.println();
//        System.out.println(geoLocation.getLatitude()+" "+geoLocation.getLongitude()) ;
        System.out.println();
        if (location == null) {
            System.out.println("dont know location");
        } else {
            System.out.println(location);
        }

//        if(location2==null)
//        System.out.println("dont know location") ;
//            else
//        System.out.println(location2) ;
        System.out.println(lat + "    " + lng);
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(Double.valueOf(lat));
        geoLocation.setLongitude(Double.valueOf(lng));
        location.setGeoLocation(geoLocation);
        return location;
    }

    @Override
    public Location addLocation(String location) {
//        Gson g=new Gson() ;
        System.out.println("inside add...................");
        JSONParser parse = new JSONParser();
        Location newLocation = new Location();
        GeoLocation geoLocation = new GeoLocation();
        try {
            JSONObject jobj = (JSONObject) parse.parse(location);
            JSONObject geoloc = (JSONObject) jobj.get("geolocation");
            String str_data1 = (String) geoloc.get("lat");
            geoLocation.setLatitude(Float.valueOf(str_data1));
            String str_data2 = (String) geoloc.get("lng");
            geoLocation.setLongitude(Float.valueOf(str_data2));
            newLocation.setGeoLocation(geoLocation);
            String name = (String) jobj.get("name");
            newLocation.setName(name);
            String details = (String) jobj.get("details");
            newLocation.setDetails(details);
            String address = (String) jobj.get("address");
            newLocation.setAddress(address);

        } catch (ParseException ex) {
            Logger.getLogger(LocationDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
//          GeoLocation geoLocation=(GeoLocation)JSON.parse(location.getGeoLocation()+"") ;
//        location.setGeoLocation(geoLocation) ;
        System.out.println(location);
        System.out.println();
        System.out.println();
//        System.out.println(location.getGeoLocation()+" ");
        newLocation = mongoOps.save(newLocation);
        return newLocation;
    }

    @Override
    public Location updateLocation(String location) {

        JSONParser parse = new JSONParser();
        Location newLocation = new Location();
        GeoLocation geoLocation = new GeoLocation();
        try {
            JSONObject jobj = (JSONObject) parse.parse(location);
            JSONObject geoloc = (JSONObject) jobj.get("geolocation");
            String str_data1 = (String) geoloc.get("lat");
            geoLocation.setLatitude(Float.valueOf(str_data1));
            String str_data2 = (String) geoloc.get("lng");
            geoLocation.setLongitude(Float.valueOf(str_data2));
            newLocation.setGeoLocation(geoLocation);
            String name = (String) jobj.get("name");
            newLocation.setName(name);
            String details = (String) jobj.get("details");
            newLocation.setDetails(details);
            String address = (String) jobj.get("address");
            newLocation.setAddress(address);

        } catch (ParseException ex) {
            Logger.getLogger(LocationDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
//      
        Query query = new Query(Criteria.where("name").is(newLocation.getName()));
        Update update = new Update();
        update.set("details", newLocation.getDetails());
        update.set("geolocation", newLocation.getGeoLocation());
        update.set("address", newLocation.getAddress());
        Location updateLocation = mongoOps.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Location.class);
        return updateLocation;
    }

    @Override
    public List<Location> findAllLocations() {
        List<Location> list = new ArrayList<Location>();
        list = mongoOps.findAll(Location.class);
        return list;
    }

}
