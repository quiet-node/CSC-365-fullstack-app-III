package yelp.dataset.oswego.yelpbackend.algorithms.haversine;

import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;

public class Haversine {

    /**
     * Calculate geographical distance between two businesses
     * @param businessModelA
     * @param businessModelB
     * @return double - the geographical distance between two nodes
     */
    public double haversine(BusinessModel businessModelA, BusinessModel businessModelB)
    {
        // Attributes
        double latitude1 = businessModelA.getLatitude();
        double longitude1 = businessModelA.getLongitude();
        double latitude2 = businessModelB.getLatitude();
        double longitude2 = businessModelB.getLongitude();
        double earthRadius = 6371;

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
