package yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph;

import java.util.*;

import lombok.Data;

@Data
public class DijkstraGraph {
    private List<DijkstraNode> nodes = new ArrayList<>();

    public void addNode(DijkstraNode node) {nodes.add(node);};
    
    public DijkstraNode getNodeByNodeID(long nodeID) {
        for(DijkstraNode node : nodes) 
            if (node.getNodeID() == nodeID) 
                return node;
        return null;
    }
}
