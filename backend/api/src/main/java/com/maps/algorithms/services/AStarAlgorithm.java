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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author  
 */
final class NodeData<String> {

    private final String nodeId;
    private final Map<String, Double> heuristic;

    private double g;  // g is distance from the source
    private double h;  // h is the heuristic of destination.
    private double f;  // f = g + h 

    public NodeData(String nodeId, Map<String, Double> heuristic) {
        this.nodeId = nodeId;
        this.g = Double.MAX_VALUE;
        this.heuristic = heuristic;
    }

    public String getNodeId() {
        return nodeId;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void calcF(String destination) {
        this.h = heuristic.get(destination);
        this.f = g + h;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return f;
    }
}

final class GraphAStar<String> implements Iterable<String> {

    private final Map<String, Map<NodeData<String>, Double>> graph;
    private final Map<String, Map<String, Double>> heuristicMap;
    private final Map<String, NodeData<String>> nodeIdNodeData;

    public GraphAStar(Map<String, Map<String, Double>> heuristicMap) {
        if (heuristicMap == null) {
            throw new NullPointerException("The huerisic map should not be null");
        }
        graph = new HashMap<String, Map<NodeData<String>, Double>>();
        nodeIdNodeData = new HashMap<String, NodeData<String>>();
        this.heuristicMap = heuristicMap;
    }

    public void addNode(String nodeId) {
        if (nodeId == null) {
            throw new NullPointerException("The node cannot be null");
        }
        if (!heuristicMap.containsKey(nodeId)) {
            throw new NoSuchElementException("This node is not a part of hueristic map");
        }

        graph.put(nodeId, new HashMap<NodeData<String>, Double>());
        nodeIdNodeData.put(nodeId, new NodeData<String>(nodeId, heuristicMap.get(nodeId)));
    }

    public void addEdge(String nodeIdFirst, String nodeIdSecond, double length) {
        if (nodeIdFirst == null || nodeIdSecond == null) {
            throw new NullPointerException("The first nor second node can be null.");
        }

        if (!heuristicMap.containsKey(nodeIdFirst) || !heuristicMap.containsKey(nodeIdSecond)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of hueristic map");
        }
        if (!graph.containsKey(nodeIdFirst) || !graph.containsKey(nodeIdSecond)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of graph");
        }
        graph.get(nodeIdFirst).put(nodeIdNodeData.get(nodeIdSecond), length);
    }

    public boolean containsNode(String nodeId) {
        if (graph.get(nodeId) == null) {
            return false;
        }
        return true;
    }

    public Map<NodeData<String>, Double> edgesFrom(String nodeId) {
        if (nodeId == null) {
            throw new NullPointerException("Stringhe input node should not be null.");
        }
        if (!heuristicMap.containsKey(nodeId)) {
            throw new NoSuchElementException("This node is not a part of hueristic map");
        }
        if (!graph.containsKey(nodeId)) {
            throw new NoSuchElementException("The node should not be null.");
        }
        return graph.get(nodeId);
    }

    public NodeData<String> getNodeData(String nodeId) {
        if (nodeId == null) {
            throw new NullPointerException("String nodeid should not be empty");
        }
        if (!nodeIdNodeData.containsKey(nodeId)) {
            throw new NoSuchElementException("The nodeId does not exist");
        }
        return nodeIdNodeData.get(nodeId);
    }

    //remove this method
    public Set<Entry<String, Map<NodeData<String>, Double>>> getEntrySet() {
        return graph.entrySet();
    }

    @Override
    public Iterator<String> iterator() {
        return graph.keySet().iterator();
    }
}

@Service
public class AStarAlgorithm implements Algorithm {

    @Autowired
    ConnectionDao connectionDao;
    @Autowired
    LocationDao locationDao;
    @Autowired
    FloydWarshallAlgorithm floydWarshallAlgorithm;

    private GraphAStar<String> graph = null;

    public class NodeComparator implements Comparator<NodeData<String>> {

        public int compare(NodeData<String> nodeFirst, NodeData<String> nodeSecond) {
            if (nodeFirst.getF() > nodeSecond.getF()) {
                return 1;
            }
            if (nodeSecond.getF() > nodeFirst.getF()) {
                return -1;
            }
            return 0;
        }
    }

    public List<String> astar(String source, String destination) {
        final Queue<NodeData<String>> openQueue = new PriorityQueue<NodeData<String>>(11, new NodeComparator());
        NodeData<String> sourceNodeData = graph.getNodeData(source);
        sourceNodeData.setG(0);
        sourceNodeData.calcF(destination);
        openQueue.add(sourceNodeData);

        final Map<String, String> path = new HashMap<String, String>();
        final Set<NodeData<String>> closedList = new HashSet<NodeData<String>>();

        while (!openQueue.isEmpty()) {

            final NodeData<String> nodeData = openQueue.poll();
            if (nodeData.getNodeId().equals(destination)) {
                return path(path, destination);
            }

            closedList.add(nodeData);
            for (Entry<NodeData<String>, Double> neighborEntry : graph.edgesFrom(nodeData.getNodeId()).entrySet()) {
                NodeData<String> neighbor = neighborEntry.getKey();
                if (closedList.contains(neighbor)) {
                    continue;
                }
                double distanceBetweenTwoNodes = neighborEntry.getValue();
                double tentativeG = distanceBetweenTwoNodes + nodeData.getG();

                if (tentativeG < neighbor.getG()) {
                    neighbor.setG(tentativeG);
                    neighbor.calcF(destination);

                    path.put(neighbor.getNodeId(), nodeData.getNodeId());
                    if (!openQueue.contains(neighbor)) {
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private List<String> path(Map<String, String> path, String destination) {
        assert path != null;
        assert destination != null;

        final List<String> pathList = new ArrayList<String>();
        pathList.add(destination);
        while (path.containsKey(destination)) {
            destination = path.get(destination);
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        return pathList;
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
//    	floydWarshallAlgorithm.floydWarshall();
        Map<String, Map<String, Double>> hueristic = floydWarshallAlgorithm.getMatrix();
        List<String> iterator = new ArrayList<String>();
        List<String> iterator2 = new ArrayList<String>();
        graph = new GraphAStar<String>(hueristic);

        for (Map.Entry<String, Map<String, Double>> entry : hueristic.entrySet()) {
            System.out.println(entry.getKey());
            iterator.add(entry.getKey());
            iterator2.add(entry.getKey());
            graph.addNode(entry.getKey());
        }

        int totalNodes = hueristic.size();

        for (String locationName : iterator) {
            Location location = locationDao.showLocationDetailsByName(locationName);
            if (location == null) {
                continue;
            }
            Map<String, Double> mapIntermediate = new HashMap<String, Double>();
            List<Location> listOfLocations = this.connectionDao.findNeighbors(locationName);
            double lat1 = location.getGeoLocation().getLatitude();
            double lng1 = location.getGeoLocation().getLongitude();
            if (listOfLocations != null) {
                for (Location loc : listOfLocations) {
                    if (loc == null) {
                        continue;
                    }
                    double lat2 = loc.getGeoLocation().getLatitude();
                    double lng2 = loc.getGeoLocation().getLongitude();
                    double dist = distance(lat1, lng1, lat2, lng2, 'M');
                    graph.addEdge(location.getName(), loc.getName(), dist);
                }
            }
        }
        System.out.println("PRINTING GRAPH");
        for (Entry<String, Map<NodeData<String>, Double>> nodes : graph.getEntrySet()) {
            System.out.println(nodes.getKey());
            for (Map.Entry<NodeData<String>, Double> st : nodes.getValue().entrySet()) {
                System.out.println("--" + st.getKey().getNodeId() + " " + st.getValue());
            }
        }
        List<Location> intermediateLocations = new ArrayList<Location>();
        for (String path : astar(source, destination)) {
            intermediateLocations.add(locationDao.showLocationDetailsByName(path));
        }

        return intermediateLocations;
    }
}
