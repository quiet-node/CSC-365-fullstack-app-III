package yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraNode;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */


@Data
@AllArgsConstructor
public class NeighborNode {
    private DijkstraNode node;
    private double distanceWeight;
    private double similarityWeight;
}
