package yelp.dataset.oswego.yelpbackend.services;

import java.io.IOException;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import yelp.dataset.oswego.yelpbackend.algorithms.clustering.KMeans;
import yelp.dataset.oswego.yelpbackend.algorithms.similarity.CosSim;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraGraph;
import yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set.DisjointUnionSets;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.d3_models.BusinessD3RootModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestBusinessModel;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

public class RestService {

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

    public Map<String, List<BusinessModel>> fetchClusters() throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        
        Map<String, List<BusinessModel>> clusters = new KMeans().initializeClusers(businessBtree, new Random().nextInt(10)+5);
        if (clusters == null) 
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return clusters;
    }

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
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);
        return new GraphService().fetchConnectedComponents(nearestNodeModels, disjointUnionSets);
    }

    public ShortestPath fetchShortestPath(int sourceNodeID, int destinationNodeID) throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);
        if (disjointUnionSets.findDisjointSet(sourceNodeID) != disjointUnionSets.findDisjointSet(destinationNodeID))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Businesses are not connected"); 

        return new GraphService().getShortestPath(sourceNodeID, destinationNodeID);
    }

    public DijkstraGraph fetchGraphByGraphID(int nodeID) throws IOException {
        new GraphService().setUpDijkstraGraph(nodeID);
        return null;
    }

}
