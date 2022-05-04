package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.Comparator;

public class WeightedEdgeComparator implements Comparator<WeightedEdge> {
    
    @Override
    public int compare(WeightedEdge o1, WeightedEdge o2) {
        if (o1.getSimilarityWeight() > o2.getSimilarityWeight()) return 1;
        if (o1.getSimilarityWeight() < o2.getSimilarityWeight()) return -1;
        return 0;
    }
}
