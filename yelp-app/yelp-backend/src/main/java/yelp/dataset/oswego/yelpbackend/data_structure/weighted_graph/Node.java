package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.List;
import lombok.Data;

@Data
public class Node {
    private int businessID;
    private List<Edge> edges;

    public Node(int businessID) {this.businessID = businessID;}

    public void addEdge(Edge edge) {this.edges.add(edge);}
}
