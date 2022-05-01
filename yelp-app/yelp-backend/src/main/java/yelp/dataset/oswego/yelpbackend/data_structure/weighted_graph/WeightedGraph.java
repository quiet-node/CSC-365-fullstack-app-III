package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModelComparator;
import yelp.dataset.oswego.yelpbackend.services.IOService;

@Data
public class WeightedGraph {
    private List<WeightedNode> nodeList;

    /**
     * Constructor - initilize nodeList i.e. form up a graph with the closest 20 businesses regard to the requested business
     * @param weightedNodesNumber
     * @throws IOException
     */
    public WeightedGraph(BusinessModel requestedBusinessModel) throws IOException {
        List<BusinessModel> businessListByDistance = businessListByDistance(requestedBusinessModel);
        nodeList = new ArrayList<>();
        for (int i = 0; i < businessListByDistance.size(); i++) nodeList.add(new WeightedNode((int)businessListByDistance.get(i).getId()));
        automatingAddingNodes(businessListByDistance);
    }

    /**
     * Retrieve a list of the neareast 20 businesses
     * @param requestedBusinessModel
     * @return
     * @throws IOException
     */
    private List<BusinessModel> businessListByDistance(BusinessModel requestedBusinessModel) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        List<BusinessModel> businessListByDistance = new ArrayList<>();    

        for (int i = 0; i < 10000; i++) {
            BusinessModel comparedBusiness = businessBtree.findKeyByBusinessID(i);
            if (requestedBusinessModel.hashCode() != comparedBusiness.hashCode()) {
                double distance = new Haversine().calculateHaversine(requestedBusinessModel, comparedBusiness);
                comparedBusiness.setDistance(distance);
                businessListByDistance.add(comparedBusiness);
            }
        }
        Collections.sort(businessListByDistance, new BusinessModelComparator());
        return businessListByDistance.subList(0, 20);
    }

    /**
     * Connect all nodes to each other. 
     * O(n*2+2)
     * @param weightedNodesNumber
     * @throws IOException
     */
    private void automatingAddingNodes(List<BusinessModel> businessListByDistance) {
        for (int i = 0; i < businessListByDistance.size(); i++) {
            for (int j = i+1; j < businessListByDistance.size(); j++) {
                double weight = getEdgeWeight(businessListByDistance.get(i), businessListByDistance.get(j));
                addWeightedEdge(i, j, weight);
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
    private void addWeightedEdge(int sourceID, int destinationID, double weight) {
        WeightedEdge edge = new WeightedEdge(nodeList.get(sourceID).getBusinessID(), nodeList.get(destinationID).getBusinessID(), weight);
        nodeList.get(sourceID).addEdge(edge);
        nodeList.get(destinationID).addEdge(edge);
    }
}
