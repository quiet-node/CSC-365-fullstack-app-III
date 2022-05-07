package yelp.dataset.oswego.yelpbackend.models.graph_models.node_models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Data
@AllArgsConstructor
public class NearestBusinessModel {
    private BusinessModel requestedBusiness;
    private List<BusinessModel> edges;
}
