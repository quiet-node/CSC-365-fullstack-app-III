package yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraNode;

@Data
@AllArgsConstructor
public class NeighborNode {
    private DijkstraNode node;
    private double distanceWeight;
    private double similarityWeight;
}
