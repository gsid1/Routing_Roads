/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maps.algorithms.model;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author  
 */
@Document(collection="Connections")
public class Connection {
    
    private String id ;
    private List<String> neighbors ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<String> neighbors) {
        this.neighbors = neighbors;
    }
    
}
