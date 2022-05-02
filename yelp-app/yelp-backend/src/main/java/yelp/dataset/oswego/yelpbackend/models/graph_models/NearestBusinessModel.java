package yelp.dataset.oswego.yelpbackend.models.graph_models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;

@Data
@AllArgsConstructor
public class NearestBusinessModel {
    private long targetBusinessID;
    private List<WeightedEdge> edges;
}
