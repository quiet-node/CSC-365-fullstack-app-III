package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.io.IOException;

import yelp.dataset.oswego.yelpbackend.algorithms.dijkstra.Dijkstra;
import yelp.dataset.oswego.yelpbackend.algorithms.dijkstra.Graph;
import yelp.dataset.oswego.yelpbackend.algorithms.dijkstra.Node;
import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.algorithms.similarity.CosSim;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set.DisjointUnionSets;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModelComparator;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestBusinessModel;

public class GraphService {

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
            // new IOService().writeNodesWithEdges(nearestNodesList.get(i)); 
        }
        // write whole list to disk -- 23.58 seconds
        // new IOService().writeNearestNodesList(nearestNodesList);
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
     * Using union-find algorithm to find connected components (subsets)
     * @return List<ConnectedComponenet>
     * @throws IOException
     * @throws InterruptedException
     */
    public List<ConnectedComponenet> fetchConnectedComponents() throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = setUpDisjoinSets(nearestNodeModels);

        // root set to find the total rootIDs
        Set<Integer> rootSet = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            rootSet.add(disjointUnionSets.findDisjointSet(i));
        }

        // turn rootSet from a HashSet to an ArrayList for easy implementations
        List<Integer> rootNodes = new ArrayList<>(new HashSet<Integer>(rootSet));

        List<ConnectedComponenet> connectedComponenets = new ArrayList<>();

        for (int i = 0; i < rootSet.size(); i++) {
            int root = rootNodes.get(i);
            List<Integer> children = new ArrayList<>();
                for (int j = 0; j < 10000; j++) {
                    if (disjointUnionSets.findDisjointSet(j) == root) {
                        children.add(j);
                    }
                }
            connectedComponenets.add(new ConnectedComponenet(root, children));
        }
        // Check if all the neighbors are connected in one set
        //System.out.println(checkConnectivity(disjointUnionSets, connectedComponenets));

        return connectedComponenets;
    }

    /**
     * Function to set up graph to prepare for dijkstra
     * @param nodeID
     * @return
     * @throws IOException
     */
    public Graph setUpDijkstraGraph(int nodeID) throws IOException {
        List<ConnectedComponenet> connectedComponenets = fetchConnectedComponents();
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = setUpDisjoinSets(nearestNodeModels);
        Graph graph = new Graph();

        ConnectedComponenet connectedComponent = getConnectedComponent(connectedComponenets, disjointUnionSets.findDisjointSet(nodeID));

        for (int connectedNodeID : connectedComponent.getChildren()) {
            WeightedNode weightedNode = new IOService().readNodesWithEdges(connectedNodeID);
            Node node = new Node(connectedNodeID);
            weightedNode.getEdges().forEach(edge -> {
                node.addDestination(new Node(edge.getDestinationID()), edge.getDistanceWeight());
            });
            graph.addNode(node);
        }

        Node rootNode = graph.getNodeByNodeID(nodeID);

        graph = new Dijkstra().calculateShortestPathFromSource(graph, new Node(nodeID));
        
        return graph;
    }


    /**
     * Function to get the connected component/disjoint set based on rootID
     * @param connectedComponenets
     * @param rootID
     * @return ConnectedComponenet
     */
    public ConnectedComponenet getConnectedComponent(List<ConnectedComponenet> connectedComponenets, int rootID) {
        for (ConnectedComponenet componenet : connectedComponenets) 
            if (componenet.getRootID() == rootID) 
                return componenet;
        return null;
    }


    /**
     * A helper function to set up the disjoint sets
     * This will read in each edge (neighbors), then union the nodes to form up disjoin sets
     * @param nearestNodeModels
     * @return DisjointUnionSets
     */
    public DisjointUnionSets setUpDisjoinSets(List<WeightedNode> nearestNodeModels) {
        DisjointUnionSets disjointUnionSets = new DisjointUnionSets();
        nearestNodeModels.forEach(model -> {
            model.getEdges().forEach(edge -> {
                int sourceRoot = disjointUnionSets.findDisjointSet((int) edge.getSourceID());
                int destinationRoot = disjointUnionSets.findDisjointSet((int) edge.getDestinationID());
                disjointUnionSets.unionDisjoinSets(sourceRoot, destinationRoot);
            });
        });
        return disjointUnionSets;
    }

    /**
     * Util method to check connectivity neighbors in each disjoint set
     * Just a helper, not neccessary
     * @param disjointUnionSets
     * @param connectedComponenets
     * @return
     * @throws IOException
     */
    public boolean checkConnectivity(DisjointUnionSets disjointUnionSets, List<ConnectedComponenet> connectedComponenets) throws IOException {
        for (ConnectedComponenet componenet : connectedComponenets) {
            int rootID = componenet.getRootID();
            List<Integer> neighbors = new ArrayList<>();
            for (Integer child : componenet.getChildren()) {
                new IOService().readNodesWithEdges(child).getEdges().forEach(edge -> {
                    neighbors.add((int) edge.getDestinationID());
                });
                for (int id : neighbors) {
                    if (disjointUnionSets.findDisjointSet(id) != rootID) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
