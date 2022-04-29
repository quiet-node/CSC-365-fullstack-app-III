package yelp.dataset.oswego.yelpbackend.dataStructure.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge implements Comparable<Edge> {
    private Node source, destination;
    private double weight;
    
    public int compareTo(Edge other) {return Double.compare(weight, other.weight);}

}
