package yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models.D3LinkModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models.D3NodeModel;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Data
@AllArgsConstructor
public class ShortestPath implements Serializable {
    private int sourceNodeID;
    private int destinationNodeID;
    private List<D3NodeModel> shortestPathNodes;
    private List<D3LinkModel> shortestPaths;
}
