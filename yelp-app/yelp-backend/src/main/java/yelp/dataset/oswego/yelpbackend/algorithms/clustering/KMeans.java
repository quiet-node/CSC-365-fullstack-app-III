package yelp.dataset.oswego.yelpbackend.algorithms.clustering;

import java.util.*;

import yelp.dataset.oswego.yelpbackend.algorithms.similarity.CosSim;
import yelp.dataset.oswego.yelpbackend.dataStructure.btree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.models.BusinessD3ChildrenModel;
import yelp.dataset.oswego.yelpbackend.models.BusinessD3Model;
import yelp.dataset.oswego.yelpbackend.models.BusinessD3RootModel;
import yelp.dataset.oswego.yelpbackend.models.BusinessModel;

public class KMeans {

    /**
     * A function to generate centroids randomly
     * @param businessBtree
     * @param k
     * @return a list of random centroids
     */
    private List<Centroid> generateRandomCentroids(BusinessBtree businessBtree, int k) {
        List<Centroid> centroids = new ArrayList<>();
        List<BusinessModel> businessList = businessBtree.findRandomBusinesses(k);

        for (int i = 0; i < businessList.size(); i++) {
            centroids.add(new Centroid( businessList.get(i).getId(), businessList.get(i).getName(), businessList.get(i).getCategories()));
        }
        
        return centroids;
    }

    /**
     * A function to find the neareest centroid
     * @param record
     * @param centroids
     * @return the nearest centroid
     */
    private Centroid nearestCentroid(BusinessModel record, List<Centroid> centroids) {
        double maximumSimRate = Double.NEGATIVE_INFINITY;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentSimRate = new CosSim().calcSimRate(record.getCategories(), centroid.getCategories());
            if (currentSimRate > maximumSimRate && record.getId() != centroid.getId()) {
                maximumSimRate = currentSimRate;
                nearest = centroid;
                record.setSimilarityRate(currentSimRate);
            }
        }
        return nearest;
    }

    /**
     * A function to assign records to clusters based on centroids. 
     * Only records with simRate > 75% will pass.
     * Each clusters will have max 10 records
     * @param clusters
     * @param record
     * @param centroid
     */
    private void assignToCluster(Map<String, List<BusinessModel>> clusters, BusinessModel record, Centroid centroid) {
        List<BusinessModel> records = clusters.get(centroid.getBusinessName());
        if (records == null) {
            records = new ArrayList<>();
        }
        if (records.size() < 10) {
            if (record.getSimilarityRate() > 0.75) {
                records.add(record);
            }
            Collections.sort(records, Collections.reverseOrder());
            clusters.put(centroid.getBusinessName(), records);
        }
    }

    /**
     * A function to initialize clusters.
     * Reference: https://www.baeldung.com/java-k-means-clustering-algorithm
     * @param businessBtree 
     * @param k number of clusters
     * @return clusters
     */
    public Map<String, List<BusinessModel>> initializeClusers(BusinessBtree businessBtree, int k) {
        Map<String, List<BusinessModel>> clusters = new HashMap<>();
        List<Centroid> centroids = generateRandomCentroids(businessBtree, k);
        List<BusinessModel> records = businessBtree.findAll();

        for (BusinessModel record : records) {
                Centroid centroid = nearestCentroid(record, centroids);
                assignToCluster(clusters, record, centroid);
        }
        return clusters;
    }

    /**
     * A function to prepare the right JSON structure for D3
     * @param businessBtree
     * @param k
     * @return BusinessD3RootModel
     */
    public BusinessD3RootModel prepareD3(BusinessBtree businessBtree, int k) {
        Map<String, List<BusinessModel>> clusters = initializeClusers(businessBtree, k);
        BusinessD3RootModel d3root = new BusinessD3RootModel();
        List<BusinessD3ChildrenModel> d3children = new ArrayList<>();
        List<String> centroids = new ArrayList<>();

        for (String centroid : clusters.keySet()) centroids.add(centroid);

        // loop through centroids
        for (String centroid : centroids) {
            // init d3Child to hold each child
            BusinessD3ChildrenModel d3Child = new BusinessD3ChildrenModel();
            d3Child.setName(centroid);

            
            if (clusters.get(centroid) != null) { // make sure this cluster has any records in it
                // init d3models to store the list of BusinessD3Model
                List<BusinessD3Model> d3models = new ArrayList<>();
                
                // loop through all the businesses in each centroid
                for (BusinessModel business : clusters.get(centroid)) {
                    BusinessD3Model d3Model = new BusinessD3Model();
                    double scale = Math.pow(10, 2);
                    d3Model.setName(business.getName() + " --- " +(Double.toString(Math.round(business.getSimilarityRate()*100*scale) / scale) + "%"));
                    d3Model.setValue((Double.toString(Math.round(business.getSimilarityRate()*100*scale) / scale) + "%")) ;
                    d3models.add(d3Model);
                }
                d3Child.setChildren(d3models);
            }
            d3children.add(d3Child);
        }
        d3root.setChildren(d3children);

        return d3root;
    }


}
