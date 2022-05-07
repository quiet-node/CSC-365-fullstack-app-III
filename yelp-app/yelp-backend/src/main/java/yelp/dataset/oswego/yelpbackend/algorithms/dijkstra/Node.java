package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;

import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;

public class Node {

    private Long nodeID;
    private List<ShortestPath> shortestPath = new ArrayList<>();
    private Double distance = Double.MAX_VALUE;
    Map<Node, Double> adjacentNodes = new HashMap<>();
        
    public Node (){};
    public Node(long sourceID) {
        this.nodeID = sourceID;
    }

    public void addDestination(Node destination, double distance) {
        adjacentNodes.put(destination, distance);
    }

    public Long getNodeID() {
        return nodeID;
    }

    public void setNodeID(Long name) {
        this.nodeID = name;
    }

    public Map<Node, Double> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<ShortestPath> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<ShortestPath> shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public String toString() {
        return "Node: "+nodeID+", distance: " + distance +", shortest path: " +shortestPath;
    }

}
