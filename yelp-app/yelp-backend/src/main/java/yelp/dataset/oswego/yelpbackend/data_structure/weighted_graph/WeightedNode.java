package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeightedNode {
    private int businessID;
    private List<WeightedEdge> edges;

    public WeightedNode(int businessID) {
        this.businessID = businessID; 
        this.edges = new ArrayList<>();
    }

    public void addEdge(WeightedEdge edge) {this.edges.add(edge);}
}
