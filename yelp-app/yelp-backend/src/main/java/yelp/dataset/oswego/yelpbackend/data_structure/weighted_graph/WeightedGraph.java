package yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;
import yelp.dataset.oswego.yelpbackend.services.WeightedGraphService;

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
        new WeightedGraphService().automatingAddingNodes(requestedBusinessList, nodeList);
    }

}
