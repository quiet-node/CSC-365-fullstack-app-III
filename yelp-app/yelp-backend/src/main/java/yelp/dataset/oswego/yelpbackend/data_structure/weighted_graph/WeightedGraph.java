package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.*;

import lombok.Data;

@Data
public class WeightedGraph {
    private Set<WeightedNode> nodeList;
    
    public void addNode(WeightedNode node) {nodeList.add(node);};
}
