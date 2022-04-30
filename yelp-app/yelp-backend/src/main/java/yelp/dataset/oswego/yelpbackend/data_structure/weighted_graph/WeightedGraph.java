package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WeightedGraph {
    private List<WeightedNode> nodeList;

    /**
     * Constructor - initilize nodeList based on number of nodes passed in
     * @param weightedNodesNumber
     */
    public WeightedGraph(int weightedNodesNumber) {
        nodeList = new ArrayList<>();
        for (int i = 0; i < weightedNodesNumber; i++) {
            nodeList.add(new WeightedNode(i));
        }
    }

    /**
     * Added weighted edge to graph
     * @param sourceID
     * @param destinationID
     * @param weight
     */
    public void addWeightedEdge(int sourceID, int destinationID, double weight) {
        WeightedEdge edge = new WeightedEdge(sourceID, destinationID, weight);
        nodeList.get(sourceID).addEdge(edge);
        nodeList.get(destinationID).addEdge(edge);
    }



}
