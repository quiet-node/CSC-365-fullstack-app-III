package yelp.dataset.oswego.yelpbackend.algorithms.haversine;

import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

public class Haversine {

    /**
     * Calculate geographical distance between two businesses
     * @param businessModelA
     * @param businessModelB
     * @return double - the geographical distance between two nodes
     */
    public double calculateHaversine(BusinessModel businessModelA, BusinessModel businessModelB)
    {
        // Attributes
        double earthRadius = 6371;
        double latitude1 = businessModelA.getLatitude();
        double longitude1 = businessModelA.getLongitude();
        double latitude2 = businessModelB.getLatitude();
        double longitude2 = businessModelB.getLongitude();

        // distance between latitudes and longitudes
        double distanceLatitude = Math.toRadians(latitude2 - latitude1);
        double distanceLongitude = Math.toRadians(longitude2 - longitude1);
 
        // convert to radians
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
 
        // apply formulae
        double answer = Math.pow(Math.sin(distanceLatitude / 2), 2) +
                   Math.pow(Math.sin(distanceLongitude / 2), 2) *
                   Math.cos(latitude1) *
                   Math.cos(latitude2);

        // final computation
        return  2 * earthRadius * Math.asin(Math.sqrt(answer));
    }
}
