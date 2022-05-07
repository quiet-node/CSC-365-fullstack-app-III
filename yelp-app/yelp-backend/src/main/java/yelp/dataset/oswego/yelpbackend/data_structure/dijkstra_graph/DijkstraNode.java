package yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph;

import java.util.*;

import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.NeighborNode;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;

public class DijkstraNode {

    private Long nodeID;
    private List<ShortestPath> shortestPath = new ArrayList<>();
    private Double distance = Double.MAX_VALUE;
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
