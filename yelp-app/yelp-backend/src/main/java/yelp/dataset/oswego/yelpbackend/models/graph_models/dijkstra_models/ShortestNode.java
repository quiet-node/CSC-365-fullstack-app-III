package yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortestNode {
    private Long nodeID;
    private double weight;
}