package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WeightedGraph {
    private List<Node> vertices;

    public WeightedGraph(int v) {
        vertices = new ArrayList<>();
    }
}
