package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;
import yelp.dataset.oswego.yelpbackend.services.IOService;

@Data
public class WeightedGraph {
    private List<WeightedNode> nodeList;

    /**
     * Constructor - initilize nodeList based on number of nodes passed in
     * @param weightedNodesNumber
     * @throws IOException
     */
    public WeightedGraph(int weightedNodesNumber) throws IOException {
        nodeList = new ArrayList<>();
        for (int i = 0; i < weightedNodesNumber; i++) nodeList.add(new WeightedNode(i));
        automatingAddingNodes(weightedNodesNumber);
    }

    /**
     * Connect all nodes to each other. 
     * O(n*2+2)
     * @param weightedNodesNumber
     * @throws IOException
     */
    private void automatingAddingNodes(int weightedNodesNumber) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        for (int i = 0; i < weightedNodesNumber; i++) {
            for (int j = i+1; j < weightedNodesNumber; j++) {
                double weight = getEdgeWeight(businessBtree, i, j);
                addWeightedEdge(i, j, weight);
            }
        }
    }

    /**
     * Calculate the geographical distances between source and destinatio nodes
     * @param businessBtree
     * @param sourceID
     * @param destinationID
     * @return
     * @throws IOException
     */
    private double getEdgeWeight(BusinessBtree businessBtree, int sourceID, int destinationID) {
        BusinessModel businessSourceNode = businessBtree.findKeyByBusinessID(sourceID);
        BusinessModel businessDestinationNode = businessBtree.findKeyByBusinessID(destinationID);
        return new Haversine().calculateHaversine(businessSourceNode, businessDestinationNode);
    }

    /**
     * Added weights to the edges
     * @param sourceID
     * @param destinationID
     * @param weight
     */
    private void addWeightedEdge(int sourceID, int destinationID, double weight) {
        WeightedEdge edge = new WeightedEdge(sourceID, destinationID, weight);
        nodeList.get(sourceID).addEdge(edge);
        nodeList.get(destinationID).addEdge(edge);
    }
}
