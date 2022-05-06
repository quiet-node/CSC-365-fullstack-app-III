package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Graph {
    private Set<Node> nodeList = new HashSet<>();
    
    public void addNode(Node node) {nodeList.add(node);};
}
