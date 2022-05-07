package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.Comparator;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

public class WeightedEdgeComparator implements Comparator<WeightedEdge> {
    
    @Override
    public int compare(WeightedEdge o1, WeightedEdge o2) {
        if (o1.getSimilarityWeight() > o2.getSimilarityWeight()) return 1;
        if (o1.getSimilarityWeight() < o2.getSimilarityWeight()) return -1;
        return 0;
    }
}
