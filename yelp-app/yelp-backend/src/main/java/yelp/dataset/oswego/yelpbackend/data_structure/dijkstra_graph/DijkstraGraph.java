package yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph;

import java.util.*;

import lombok.Data;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

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
