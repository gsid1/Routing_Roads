/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maps.algorithms.dao;

import com.maps.algorithms.model.Location;
import java.util.List;

/**
 *
 * @author  
 */
public interface ConnectionDao {
    
    public List<Location> findNeighbors(String name) ;
}
