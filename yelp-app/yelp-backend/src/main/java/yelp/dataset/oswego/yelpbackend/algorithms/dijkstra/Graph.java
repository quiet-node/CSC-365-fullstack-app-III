package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Graph {
    private List<Node> nodeList = new ArrayList<>();
    
    public void addNode(Node node) {nodeList.add(node);};

    public Node getNodeByNodeID(long nodeID) {
        for(Node node : nodeList) 
            if (node.getNodeID() == nodeID) 
                return node;
        return null;
    }
}
