package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.io.IOException;

import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModelComparator;

public class WeightedGraphService {
    /**
     * Retrieve a list of the neareast 100 businesses
     * @param requestedBusinessModel
     * @return
     * @throws IOException
     */
    public List<BusinessModel> businessListByDistance(BusinessModel requestedBusinessModel) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        List<BusinessModel> businessListByDistance = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            BusinessModel comparedBusiness = businessBtree.findKeyByBusinessID(i);
            if (requestedBusinessModel.getId() != comparedBusiness.getId()) {
                double distance = new Haversine().calculateHaversine(requestedBusinessModel, comparedBusiness);
                comparedBusiness.setDistance(distance);
                businessListByDistance.add(comparedBusiness);
            }
        }
        Collections.sort(businessListByDistance, new BusinessModelComparator());
        return businessListByDistance.subList(0, 100);
    }

    /**
     * Connect all nodes to each other. 
     * O(n*2+2)
     * @param weightedNodesNumber
     * @throws IOException
     */
    public void automatingAddingNodes(List<BusinessModel> businessListByDistance, List<WeightedNode> nodeList) {
        for (int i = 0; i < businessListByDistance.size(); i++) {
            for (int j = i+1; j < businessListByDistance.size(); j++) {
                double weight = getEdgeWeight(businessListByDistance.get(i), businessListByDistance.get(j));
                addWeightedEdge(nodeList, i, j, weight);
            }
        }
    }

    /**
     * Calculate the geographical distances between source and destinatio nodes
     * @param businessBtree
     * @param sourceID
     * @param destinationID
     * @return double - the distance between two nodes
     * @throws IOException
     */
    private double getEdgeWeight(BusinessModel businessSource, BusinessModel businessDestination) {
        return new Haversine().calculateHaversine(businessSource, businessDestination);
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
