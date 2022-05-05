package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.*;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;

@Data
public class WeightedGraph {
    private BusinessModel requestedBusinessModel;
    private List<WeightedNode> nodeList;

}
