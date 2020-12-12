/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maps.algorithms.services;

import com.maps.algorithms.dao.ConnectionDao;
import com.maps.algorithms.dao.LocationDao;
import com.maps.algorithms.model.Location;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author  
 */
@Service
public class DijkstrasAlgorithm implements Algorithm {

    @Autowired
    ConnectionDao connectionDao;
    @Autowired
    LocationDao locationDao;
    @Autowired
    FloydWarshallAlgorithm floydWarshallAlgorithm;

    private static final int MAX = 100000;

    public class Node {

        private String name;
        private List<Node> shortestPath = new LinkedList<>();
        private double distance = Double.MAX_VALUE;
        private Map<Node, Double> adjacentNodes = new HashMap<>();

        public void addDestination(Node destination, double distance) {
            adjacentNodes.put(destination, distance);
        }

        public Node(String name) {
            this.name = name;
        }

        public List<Node> getShortestPath() {
            return shortestPath;
        }

        public void setShortestPath(List<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }

        public Map<Node, Double> getAdjacentNodes() {
            return adjacentNodes;
        }

        public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
            this.adjacentNodes = adjacentNodes;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        double lowestDistance = Double.MAX_VALUE;
        for (Node node : unsettledNodes) {
            double nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    public class Graph {

        private Set<Node> nodes = new HashSet<>();

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }

        public Set<Node> getNodes() {
            return nodes;
        }
    }

    private static void calculateMinimumDistance(Node evaluationNode, Double edgeWeigh, Node sourceNode) {
        if (evaluationNode == null) {
            throw new NullPointerException("evaluation Node cannot be null to add it to shortest path of from source");
        }
        if (sourceNode == null) {
            throw new NullPointerException("source Node cannot be null to calulate minimum distance");
        }
        Double sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
        if (graph == null) {
            throw new NullPointerException("Graph can not be null to calculate shortest path");
        }
        if (source == null) {
            throw new NullPointerException("source can not be null to calculate shortest path");
        }
        source.setDistance(0.0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);
        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Double> adjacencyPair
                    : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Double edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        } else if (unit == 'M') {
            dist = dist * 1609.344;
        }
        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    public List<Location> calculatePath(String source, String destination) {
        if (source == null) {
            throw new NullPointerException("source cannot be null to calculate path between source and destination");
        }
        if (destination == null) {
            throw new NullPointerException("destination cannot be null to calculate path between source and destination");
        }

        Iterator<String> itr = null;
        itr = floydWarshallAlgorithm.getIterator();
        Location sourceLocation = locationDao.showLocationDetailsByName(source);
        Map<String, Node> nodes = new HashMap<String, Node>();
        Node sourceNode = new Node(source);
        List<Location> list = new ArrayList<>();
        while (itr.hasNext()) {
            String crt = itr.next();
            Location current = locationDao.showLocationDetailsByName(crt);
            list.add(current);
        }
        for (Location location : list) {
            nodes.put(location.getName(), new Node(location.getName()));
        }
        for (Location location : list) {
            List<Location> listOfLocations
                    = connectionDao.findNeighbors(location.getName());
            if (listOfLocations != null) {
                for (Location loc : listOfLocations) {
                    double lat1 = location.getGeoLocation().getLatitude();
                    double lng1 = location.getGeoLocation().getLongitude();
                    double lat2 = loc.getGeoLocation().getLatitude();
                    double lng2 = loc.getGeoLocation().getLongitude();
                    Node current = nodes.get(location.getName());
                    Node nextNode = nodes.get(loc.getName());
                    current.addDestination(nextNode, distance(lat1, lng1, lat2, lng2, 'M'));
                }
            }
        }

        Graph graph = new Graph();
        for (Map.Entry<String, Node> node : nodes.entrySet()) {
            graph.addNode(nodes.get(node.getKey()));
            if (nodes.get(node.getKey()).getName().equals(source)) {
                sourceNode = nodes.get(node.getKey());
            }
        }
        for (Node n : graph.getNodes()) {
            System.out.println(n.getName());
            for (Entry<Node, Double> adjacencyPair
                    : n.getAdjacentNodes().entrySet()) {
                System.out.println("--" + adjacencyPair.getKey().getName());
            }
        }
        for (Entry<Node, Double> adjacencyPair
                : sourceNode.getAdjacentNodes().entrySet()) {
            System.out.println("--" + adjacencyPair.getKey().getName());
        }
        graph = DijkstrasAlgorithm.calculateShortestPathFromSource(graph, sourceNode);
        List<Location> result = new ArrayList();
        for (Node finalNode : graph.nodes) {
            if (finalNode.getName().equals(destination)) {
                for (Node nextNode : finalNode.getShortestPath()) {
                    Location loc = locationDao.showLocationDetailsByName(nextNode.getName());
                    result.add(loc);
                }
                Location dest = locationDao.showLocationDetailsByName(destination);
                result.add(dest);
            } else {
                for (Node nextNode : finalNode.getShortestPath()) {
                    Location loc = locationDao.showLocationDetailsByName(nextNode.getName());
                }
            }
        }
        return result;
    }
}
