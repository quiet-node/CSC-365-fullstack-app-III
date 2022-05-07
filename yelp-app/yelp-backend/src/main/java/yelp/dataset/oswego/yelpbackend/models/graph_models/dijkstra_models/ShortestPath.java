package yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models;

import java.util.List;

import lombok.Data;

@Data
public class ShortestPath {
    private Long sourceNodeID;
    private Long destiantionNodeID;
    private List<ShortestNode> shortestPath;
}
