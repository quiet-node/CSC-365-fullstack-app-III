package yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph;

import java.io.Serializable;
import java.util.*;

import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.NeighborNode;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestNode;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

public class DijkstraNode implements Serializable {

    private Long nodeID;
    private List<ShortestNode> shortestPath = new ArrayList<>();
    private Double weight = Double.MAX_VALUE;
    List<NeighborNode> neighborNodes = new ArrayList<>();
        
    public DijkstraNode (){};
    public DijkstraNode(long sourceID) {
        this.nodeID = sourceID;
    }

    public void addDestination(NeighborNode neighborNode) {
        neighborNodes.add(neighborNode);
    }

    public Long getNodeID() {
        return nodeID;
    }

    public void setNodeID(Long name) {
        this.nodeID = name;
    }

    public List<NeighborNode> getNeighborNodes() {
        return neighborNodes;
    }

    public void setNeighborNodes(List<NeighborNode> neighborNodes) {
        this.neighborNodes = neighborNodes;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<ShortestNode> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<ShortestNode> shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public String toString() {
        return "Node: "+nodeID+", weight: " + weight +", shortest path: " +shortestPath;
    }

}
