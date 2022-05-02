package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.io.IOException;

import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModelComparator;

public class GraphService {

    /**
     * Find four geographically nearest businesses based on business name
     * @param requestedBusinessModel
     * @return List<BusinessModel>
     * @throws IOException
     */
    public List<BusinessModel> getClosestFour(BusinessModel requestedBusinessModel) throws IOException {
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
        return businessListByDistance.subList(0, 4);
    }

    
}
