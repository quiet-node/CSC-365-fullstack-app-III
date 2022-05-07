package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;

@Data
public class Node {
    private Long nodeID;
    private List<Node> shortestPath = new ArrayList<>();
    private Double distance = Double.MAX_VALUE;
    Map<Node, Double> adjacentNodes = new HashMap<>();
    // private List<WeightedEdge> edges;
     
    public Node(long sourceID) {
        this.nodeID = sourceID;
    }

    public void addDestination(Node destination, double distance) {
        adjacentNodes.put(destination, distance);
    }

}
