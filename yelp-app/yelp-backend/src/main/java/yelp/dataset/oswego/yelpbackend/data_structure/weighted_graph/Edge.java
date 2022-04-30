package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge implements Comparable<Edge> {
    private int source, destination;
    private double weight;
    public int compareTo(Edge other) {return Double.compare(this.weight, other.weight);}
}