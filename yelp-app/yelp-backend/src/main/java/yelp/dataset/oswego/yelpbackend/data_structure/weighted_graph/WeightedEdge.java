package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightedEdge implements Comparable<WeightedEdge> {
    private long sourceID, destinationID;
    private double distanceWeight, similarityWeight;

    @Override
    public int compareTo(WeightedEdge other) {
        return Double.compare(this.distanceWeight, other.distanceWeight);
    }
}
