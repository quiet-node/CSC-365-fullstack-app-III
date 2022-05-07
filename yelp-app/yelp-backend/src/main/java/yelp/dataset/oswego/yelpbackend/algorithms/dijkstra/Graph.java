package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Graph {
    private List<Node> nodes = new ArrayList<>();
    public void addNode(Node node) {nodes.add(node);};
    public Node getNodeByNodeID(long nodeID) {
        for(Node node : nodes) 
            if (node.getNodeID() == nodeID) 
                return node;
        return null;
    }
}
