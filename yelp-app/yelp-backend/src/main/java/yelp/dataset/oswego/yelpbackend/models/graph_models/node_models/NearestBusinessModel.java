package yelp.dataset.oswego.yelpbackend.models.graph_models.node_models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;

@Data
@AllArgsConstructor
public class NearestBusinessModel {
    private BusinessModel requestedBusiness;
    private List<BusinessModel> edges;
}
