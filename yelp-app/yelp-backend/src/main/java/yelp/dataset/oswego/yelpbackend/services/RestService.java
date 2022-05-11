package yelp.dataset.oswego.yelpbackend.services;

import java.io.IOException;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import yelp.dataset.oswego.yelpbackend.algorithms.clustering.KMeans;
import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.algorithms.similarity.CosSim;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraGraph;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraNode;
import yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set.DisjointUnionSets;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModelComparator;
import yelp.dataset.oswego.yelpbackend.models.d3_models.BusinessD3RootModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestBusinessModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models.D3GraphModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models.D3LinkModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models.D3NodeModel;

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


    /**
     * Find four geographically nearest businesses for each business and write it to disk
     * @param numberOfNodes
     * @return List<NearestBusinessModel>
     * @throws IOException
     */
    public void writeClosestFourToDataStore(int numberOfNodes) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        List<WeightedNode> nearestNodesList = new ArrayList<>();
        
        for (int i = 0; i < numberOfNodes; i++) {
            List<BusinessModel> closestFourBusinessNodelList = new ArrayList<>();
            List<WeightedEdge> edges = new ArrayList<>();
            BusinessModel targetBusiness = businessBtree.findKeyByBusinessID(i);
            for (int j = 0; j < numberOfNodes; j++) {
                BusinessModel comparedBusiness = businessBtree.findKeyByBusinessID(j);
                if (targetBusiness.getId() != comparedBusiness.getId()){
                    double distanceWeight = new Haversine().calculateHaversine(targetBusiness, comparedBusiness);
                    comparedBusiness.setDistance(distanceWeight);
                    closestFourBusinessNodelList.add(comparedBusiness);
                }
            }
            Collections.sort(closestFourBusinessNodelList, new BusinessModelComparator());
            closestFourBusinessNodelList = closestFourBusinessNodelList.subList(0, 4);
            for (BusinessModel businessModel : closestFourBusinessNodelList) {
                double simRateWeight = new CosSim().calcSimRate(targetBusiness.getCategories(), businessModel.getCategories());
                WeightedEdge weightedEdge = new WeightedEdge(targetBusiness.getId(), businessModel.getId(), businessModel.getDistance(), simRateWeight);
                edges.add(weightedEdge);
            }
            nearestNodesList.add(new WeightedNode(targetBusiness.getId(), edges));
            
            // write each node to disk -- this takes 4 minutes to finish
            new IOService().writeNodesWithEdges(nearestNodesList.get(i)); 
        }
        // write whole list to disk -- 23.58 seconds
        new IOService().writeNearestNodesList(nearestNodesList);
    }

    /**
     * Find four geographically nearest businesses based on business name
     * @param requestedBusinessModel
     * @return List<BusinessModel>
     * @throws IOException
     */
    public NearestBusinessModel fetchClosestFourByBusinessName(BusinessModel requestedBusinessModel) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        WeightedNode nearestNodeModel = new IOService().readNodesWithEdges(requestedBusinessModel.getId());
        List<BusinessModel> businessModelEdges = new ArrayList<>();

        nearestNodeModel.getEdges().forEach(edge -> {
            BusinessModel destinationBusinessModel = businessBtree.findKeyByBusinessID((int) edge.getDestinationID());
            destinationBusinessModel.setDistance(edge.getDistanceWeight());
            
            double similarityRate = new CosSim().calcSimRate(requestedBusinessModel.getCategories(), destinationBusinessModel.getCategories());
            destinationBusinessModel.setSimilarityRate(similarityRate);
            
            businessModelEdges.add(destinationBusinessModel);
        });

        return new NearestBusinessModel(requestedBusinessModel, businessModelEdges);
    }

    /**
     * Gets closest four businesses by ID
     * @param businessID
     * @return
     * @throws IOException
     */
    public WeightedNode getClosestFourByBusinessID(Long businessID) throws IOException {
        return new IOService().readNodesWithEdges(businessID);
    }

    /**
     * Get closest four businesses with edges for all 10,000 businesses
     * @return
     * @throws IOException
     */
    public List<WeightedNode> getClosestFourNodeList() throws IOException {
        return new IOService().readNearestNodesList();
    }

     /**
     * A function to write each dijkstra graph to disk
     * @throws IOException
     */
    public void writeDijkstraGraph() throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);

        List<ConnectedComponenet> connectedComponenets = new GraphService().fetchConnectedComponents(nearestNodeModels, disjointUnionSets);

        for (ConnectedComponenet connectedComponenet : connectedComponenets) {
            if (connectedComponenet.getChildren().size() < 20) {
                for(int nodeID : connectedComponenet.getChildren()){
                    DijkstraGraph dijkstraGraph = new GraphService().getDijkstraGraph(nodeID);
                    // write each dijkstra to disk
                    new IOService().writeDijkstraGraph(dijkstraGraph, nodeID);
                    // System.out.println(nodeID);
                }
            }
        }
    }
    
    /**
     * Get a list of disjoint sets
     * @return
     * @throws IOException
     */
    public List<ConnectedComponenet> fetchConnectedComponents() throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);
        return new GraphService().fetchConnectedComponents(nearestNodeModels, disjointUnionSets);
    }

    /**
     * Main method to get the shortest path of the two chosen nodes
     * @param sourceNodeID
     * @param destinationNodeID
     * @return
     * @throws IOException
     */
    public ShortestPath fetchShortestPath(int sourceNodeID, int destinationNodeID) throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);
        if (disjointUnionSets.findDisjointSet(sourceNodeID) != disjointUnionSets.findDisjointSet(destinationNodeID))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Businesses are not connected"); 
        
        DijkstraGraph dijkstraGraph = new IOService().readDijkstraGraph(sourceNodeID);

        List<D3LinkModel> shortestPaths = new ArrayList<>();
        List<D3NodeModel> shortestPathNodes = new ArrayList<>();

        for (DijkstraNode node : dijkstraGraph.getNodes())
            if (node.getNodeID() == destinationNodeID){
                for (int i = 0; i <= node.getShortestPath().size(); i++) {
                    if (i == node.getShortestPath().size()-1) {
                        shortestPaths.add(new D3LinkModel(node.getShortestPath().get(i).getNodeID(), destinationNodeID));
                        shortestPathNodes.add(new D3NodeModel(node.getShortestPath().get(i).getNodeID().intValue()));
                        break;
                    } else {
                        shortestPaths.add(new D3LinkModel(node.getShortestPath().get(i).getNodeID(), node.getShortestPath().get(i+1).getNodeID()));
                    }
                    shortestPathNodes.add(new D3NodeModel(node.getShortestPath().get(i).getNodeID().intValue()));
                }
            }
            shortestPathNodes.add(new D3NodeModel(destinationNodeID));
        return new ShortestPath(sourceNodeID, destinationNodeID, shortestPathNodes, shortestPaths);
    }

    /**
     * Prepares Data for React D3 Graph (RD3G)
     * @return
     * @throws IOException
     */
    public D3GraphModel fetchRd3gData() throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);
        List<ConnectedComponenet> connectedComponenets = new GraphService().fetchConnectedComponents(nearestNodeModels, disjointUnionSets);

        List<Integer> rootIDs = new ArrayList<>(new IOService().fetchDijkstraRootIDs());
        int randomRootID = disjointUnionSets.findDisjointSet(rootIDs.get(new Random().nextInt(rootIDs.size())));

        Set<D3NodeModel> d3Nodes = new HashSet<>();
        ConnectedComponenet connectedComponenet = new GraphService().getConnectedComponent(connectedComponenets, randomRootID);
        for(Integer child : connectedComponenet.getChildren()) 
            d3Nodes.add(new D3NodeModel(child));

        Set<D3LinkModel> D3links = new HashSet<>();
        DijkstraGraph graph = new GraphService().initGraph(randomRootID);
        graph.getNodes().forEach(node -> {
            Long d3SourceID = node.getNodeID();
            node.getNeighborNodes().forEach(neighbor -> {
                    Long d3TargetID = neighbor.getNode().getNodeID();
                    D3links.add(new D3LinkModel(d3SourceID, d3TargetID));
                }
            );
        });

        return new D3GraphModel(d3Nodes, D3links);
    }
}
