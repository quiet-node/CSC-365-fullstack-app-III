package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeightedNode implements Serializable {
    private long nodeID;
    private List<WeightedEdge> edges;
}
