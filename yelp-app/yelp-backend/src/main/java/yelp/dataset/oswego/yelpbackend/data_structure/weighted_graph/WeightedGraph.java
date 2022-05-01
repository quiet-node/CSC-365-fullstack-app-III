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
    public WeightedGraph(List<BusinessModel> requestedBusinessList) throws IOException {
        nodeList = new ArrayList<>();
        for (int i = 0; i < requestedBusinessList.size(); i++) nodeList.add(new WeightedNode((int)requestedBusinessList.get(i).getId()));
        new WeightedGraphService().automatingAddingNodes(requestedBusinessList, nodeList);
    }

}
