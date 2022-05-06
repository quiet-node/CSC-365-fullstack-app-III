package yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;

@Data
@AllArgsConstructor
public class ConnectedComponenet {
    private int rootID;
    private List<WeightedNode> children;
}
