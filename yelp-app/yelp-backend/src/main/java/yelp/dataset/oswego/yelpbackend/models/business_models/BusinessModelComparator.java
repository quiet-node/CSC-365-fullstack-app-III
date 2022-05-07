package yelp.dataset.oswego.yelpbackend.models.business_models;

import java.util.Comparator;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

public class BusinessModelComparator implements Comparator<BusinessModel> {
    @Override
    public int compare(BusinessModel o1, BusinessModel o2) {
        if (o1.getDistance() > o2.getDistance()) return 1;
        if (o1.getDistance() < o2.getDistance()) return -1;
        return 0;
    }
}
