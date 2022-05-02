package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.io.IOException;

import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModelComparator;
import yelp.dataset.oswego.yelpbackend.models.graph_models.NearestBusinessModel;

public class GraphService {

    /**
     * Find four geographically nearest businesses for each business
     * @param numNode
     * @return Map<Long, List<WeightedEdge>>
     * @throws IOException
     */
    public List<NearestBusinessModel> getClosestFourHashMap(int numNode) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        List<NearestBusinessModel> closestFourHashMap = new ArrayList<>();
        for (int i = 0; i < numNode; i++) {
            List<BusinessModel> closestFourList = new ArrayList<>();
            List<WeightedEdge> edges = new ArrayList<>();
            BusinessModel targetBusiness = businessBtree.findKeyByBusinessID(i);
            for (int j = 0; j < numNode; j++) {
                BusinessModel comparedBusiness = businessBtree.findKeyByBusinessID(j);
                if (targetBusiness.getId() != comparedBusiness.getId()){
                    double weight = new Haversine().calculateHaversine(targetBusiness, comparedBusiness);
                    comparedBusiness.setDistance(weight);
                    closestFourList.add(comparedBusiness);
                }
            }
            Collections.sort(closestFourList, new BusinessModelComparator());
            closestFourList = closestFourList.subList(0, 4);
            for (BusinessModel businessModel : closestFourList) {
                WeightedEdge weightedEdge = new WeightedEdge(targetBusiness.getId(), businessModel.getId(), businessModel.getDistance());
                edges.add(weightedEdge);
            }
            closestFourHashMap.add(new NearestBusinessModel(targetBusiness.getId(), edges));
        }
        return closestFourHashMap;
    }


        /**
     * Find four geographically nearest businesses based on business name
     * @param requestedBusinessModel
     * @return List<BusinessModel>
     * @throws IOException
     */
    public List<BusinessModel> getClosestFour(BusinessModel requestedBusinessModel) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        List<BusinessModel> closestFourList = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            BusinessModel comparedBusiness = businessBtree.findKeyByBusinessID(i);
            if (requestedBusinessModel.getId() != comparedBusiness.getId()) {
                double distance = new Haversine().calculateHaversine(requestedBusinessModel, comparedBusiness);
                comparedBusiness.setDistance(distance);
                closestFourList.add(comparedBusiness);
            }
        }
        Collections.sort(closestFourList, new BusinessModelComparator());
        return closestFourList.subList(0, 4);
    }
    
}
