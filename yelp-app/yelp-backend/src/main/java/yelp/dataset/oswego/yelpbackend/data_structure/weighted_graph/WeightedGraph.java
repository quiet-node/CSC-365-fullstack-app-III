package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;
import yelp.dataset.oswego.yelpbackend.services.WeightedGraphService;

@Data
public class WeightedGraph {
    private List<WeightedNode> nodeList;

    /**
     * Constructor - initilize nodeList i.e. form up a graph with the closest 100 businesses regard to the requested business
     * @param weightedNodesNumber
     * @throws IOException
     */
    public WeightedGraph(BusinessModel requestedBusinessModel) throws IOException {
        List<BusinessModel> businessListByDistance = new WeightedGraphService().businessListByDistance(requestedBusinessModel);
        nodeList = new ArrayList<>();
        for (int i = 0; i < businessListByDistance.size(); i++) nodeList.add(new WeightedNode((int)businessListByDistance.get(i).getId()));
        new WeightedGraphService().automatingAddingNodes(businessListByDistance, nodeList);
    }

    /**
     * Get four geographically nearest businesses
     * @param requestedBusinessModel
     * @return
     * @throws IOException
     */
    public List<BusinessModel> getClosestFour(BusinessModel requestedBusinessModel) throws IOException {
        return new WeightedGraphService().businessListByDistance(requestedBusinessModel).subList(0, 4);
    }

}
