package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightedEdge implements Comparable<WeightedEdge>, Serializable {
    private long sourceID, destinationID;
    private double distanceWeight, similarityWeight;

    @Override
    public int compareTo(WeightedEdge other) {
        return Double.compare(this.distanceWeight, other.distanceWeight);
    }
}
