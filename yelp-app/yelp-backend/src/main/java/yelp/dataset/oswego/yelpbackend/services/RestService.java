package yelp.dataset.oswego.yelpbackend.services;

import java.io.IOException;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import yelp.dataset.oswego.yelpbackend.algorithms.clustering.KMeans;
import yelp.dataset.oswego.yelpbackend.algorithms.similarity.CosSim;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.Graph;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.d3_models.BusinessD3RootModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestBusinessModel;


public class RestService {
    /**
     * A function to get the similar businesses to the target business
     * @param allBusinesses
     * @param targetB
     * @return a list of similar businesses
     */
    public List<BusinessModel> getSimilarBusinesses(List<BusinessModel> allBusinesses , BusinessModel targetB) {
        List<BusinessModel> similarBusinesses = new ArrayList<BusinessModel>();
        CosSim cosSim = new CosSim();

        for (BusinessModel business : allBusinesses) {
            double cosSimRate = cosSim.calcSimRate(targetB.getCategories(), business.getCategories());
            business.setSimilarityRate(cosSimRate);  
                if (business.getSimilarityRate() >= 0.75 && business.getSimilarityRate() <= 1) {
                           similarBusinesses.add(business);
                }
        }

        Collections.sort(similarBusinesses, Collections.reverseOrder());
        return similarBusinesses;
    }

    /**
     * A function to fetch random clusters
     * @return clusters
     * @throws IOException
     */
    public Map<String, List<BusinessModel>> fetchClusters() throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        
        Map<String, List<BusinessModel>> clusters = new KMeans().initializeClusers(businessBtree, new Random().nextInt(10)+5);
        if (clusters == null) 
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return clusters;
    }

    /**
     * A function to build the right JSON strcuture for D3
     * @return root JSON structure for D3
     * @throws IOException
     */
    public BusinessD3RootModel prepareD3() throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        BusinessD3RootModel d3Root = new KMeans().prepareD3(businessBtree, new Random().nextInt(10)+5);
        if (d3Root == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return d3Root;
    }


    public NearestBusinessModel getClosestFourByBusinessName(BusinessModel requestedBusinessModel) throws IOException {
        return new GraphService().fetchClosestFourByBusinessName(requestedBusinessModel);
    }
    public WeightedNode getClosestFourByBusinessID(Long businessID) throws IOException {
        return new IOService().readNodesWithEdges(businessID);
    }
    public List<WeightedNode> getClosestFourNodeList() throws IOException {
        return new IOService().readNearestNodesList();
    }
    public List<ConnectedComponenet> fetchConnectedComponents() throws IOException {
        return new GraphService().fetchConnectedComponents();
    }
    public Graph fetchGraph(int nodeID) throws IOException {
        return new GraphService().setUpDijkstraGraph(nodeID);
    }

}
