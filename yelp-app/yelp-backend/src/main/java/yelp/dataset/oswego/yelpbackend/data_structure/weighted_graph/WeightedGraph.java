package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;

@Data
public class WeightedGraph {
    private BusinessModel requestedBusinessModel;
    private List<WeightedNode> nodeList;

    /**
     * Constructor - initilize nodeList i.e. form up a graph with the closest businesses regard to the requested business (include the business)
     * @param weightedNodesNumber
     * @throws IOException
     */
    public WeightedGraph(BusinessModel requestedBusinessModel, List<BusinessModel> requestedBusinessList) throws IOException {
        this.requestedBusinessModel = requestedBusinessModel;
        requestedBusinessList.add(0, requestedBusinessModel);
        nodeList = new ArrayList<>();
        for (int i = 0; i < requestedBusinessList.size(); i++) nodeList.add(new WeightedNode((int)requestedBusinessList.get(i).getId()));
        automatingAddingNodes(requestedBusinessList, nodeList);
    }

    /**
     * Connect all nodes to each other. 
     * Greedy algorithm
     * O(n*2+2)
     * @param weightedNodesNumber
     * @throws IOException
     */
    private void automatingAddingNodes(List<BusinessModel> businessListByDistance, List<WeightedNode> nodeList) {
        for (int i = 0; i < businessListByDistance.size(); i++) {
            for (int j = i+1; j < businessListByDistance.size(); j++) {
                double weight = new Haversine().calculateHaversine(businessListByDistance.get(i), businessListByDistance.get(j));
                addWeightedEdge(nodeList, i, j, weight);
            }
        }
    }

    /**
     * Added weights to the edges
     * @param sourceID
     * @param destinationID
     * @param weight
     */
    private void addWeightedEdge(List<WeightedNode> nodeList, int sourceID, int destinationID, double weight) {
        WeightedEdge edge = new WeightedEdge(nodeList.get(sourceID).getBusinessID(), nodeList.get(destinationID).getBusinessID(), weight);
        nodeList.get(sourceID).addEdge(edge);
        nodeList.get(destinationID).addEdge(edge);
    }
}
