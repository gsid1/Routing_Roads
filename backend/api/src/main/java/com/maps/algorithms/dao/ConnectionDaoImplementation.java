/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maps.algorithms.dao;

import com.maps.algorithms.model.Connection;
import com.maps.algorithms.model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author  
 */
@Repository("connectionDao")
public class ConnectionDaoImplementation implements ConnectionDao {

    @Autowired
    private MongoOperations mongoOps;
    @Autowired
    private LocationDao locationDao;

    @Override
    public List<Location> findNeighbors(String name) {
//        System.out.println("inside find neighbors") ;
        List<Location> list = new ArrayList();
        Query searchUserQuery = new Query(Criteria.where("_id").is(name));
        List<String> neighbors = null;
        Connection connection = mongoOps.findOne(searchUserQuery, Connection.class);
        if(connection!=null)
        neighbors = connection.getNeighbors();
        if (neighbors != null) {
            for (String near : neighbors) {
                Location location = locationDao.showLocationDetailsByName(near);
                list.add(location);
            }
        }
        return list;
    }

}
