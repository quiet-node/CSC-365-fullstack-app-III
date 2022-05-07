package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Data
@AllArgsConstructor
public class WeightedNode implements Serializable {
    private long nodeID;
    private List<WeightedEdge> edges;
}
