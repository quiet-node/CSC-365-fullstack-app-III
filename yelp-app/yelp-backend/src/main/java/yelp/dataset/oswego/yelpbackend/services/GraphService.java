package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import yelp.dataset.oswego.yelpbackend.algorithms.dijkstra.Dijkstra;
import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.algorithms.similarity.CosSim;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraGraph;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraNode;
import yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set.DisjointUnionSets;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModelComparator;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.NeighborNode;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestBusinessModel;


/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

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
     * A function to write each dijkstra graph to disk
     * @throws IOException
     */
    public void writeDijkstraGraph() throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);

        List<ConnectedComponenet> connectedComponenets = new GraphService().fetchConnectedComponents(nearestNodeModels, disjointUnionSets);
        int count = 0;
        long totalTime = 0;
        int totalNodes = 0;
        for (ConnectedComponenet connectedComponenet : connectedComponenets) {
            count +=1;
            Instant before = Instant.now();
            
            DijkstraGraph dijkstraGraph = setUpDijkstraGraph(connectedComponenet.getRootID());
            int nodesSize = dijkstraGraph.getNodes().size();
            totalNodes += nodesSize;
            
            Instant after = Instant.now();
            long differenceInMillis = Duration.between(before, after).toMillis();
            long differenceInSeconds = Duration.between(before, after).toSeconds();
            long differenceInMinutes = Duration.between(before, after).toMinutes();
            long differenceInHours = Duration.between(before, after).toHours();
            totalTime += differenceInMillis;

            System.out.println(count+ ". Disjoint set: " +connectedComponenet.getRootID()+ ". Total nodes: " +nodesSize+ " ---- " +differenceInMillis+ " millis ---- " +differenceInSeconds+ " seconds ---- " +differenceInMinutes+ " minutes ---- " +differenceInHours+ " hours.");
            System.out.println();
            
            // write each dijkstra to disk -- this takes ... to finish
            new IOService().writeDijkstraGraph(dijkstraGraph, connectedComponenet.getRootID());
        }
        System.out.println("Total: " +count+ "disjoint sets, " +totalNodes+ " nodes --- took " +totalTime / 3600000+ "hours to finish");
    }

    /**
     * Main method to get the shortest path of the two chosen nodes
     * @param sourceNodeID
     * @param destinationNodeID
     * @return
     * @throws IOException
     */
    public ShortestPath getShortestPath(int sourceNodeID, int destinationNodeID) throws IOException {
        DijkstraGraph dijkstraGraph = new IOService().readDijkstraGraph(sourceNodeID);
        for (DijkstraNode node : dijkstraGraph.getNodes())
            if (node.getNodeID() == destinationNodeID)
                return new ShortestPath(sourceNodeID, destinationNodeID, node.getShortestPath());
        return null;
    }

    /**
     * Function to set up graph to run dijkstra
     * @param nodeID
     * @return
     * @throws IOException
     */
    public DijkstraGraph setUpDijkstraGraph(int nodeID) throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);

        List<ConnectedComponenet> connectedComponenets = new GraphService().fetchConnectedComponents(nearestNodeModels, disjointUnionSets);

        // get specific disjoint set
        ConnectedComponenet connectedComponent = new GraphService().getConnectedComponent(connectedComponenets, disjointUnionSets.findDisjointSet(nodeID));

        // a list of all the nodes prepare for the graph for dijkstra
        List<DijkstraNode> graphNodes = new ArrayList<>();

        for (int connectedNodeID : connectedComponent.getChildren()) {
            graphNodes.add(new DijkstraNode(connectedNodeID));
        }

        DijkstraGraph graph = new DijkstraGraph();

        for (DijkstraNode node : graphNodes) {
            // Now each node is already written on disk (csv files), use IOService to retrieve each node with their neighbors
            WeightedNode weightedNode = new IOService().readNodesWithEdges(node.getNodeID());

            for (WeightedEdge edge : weightedNode.getEdges()) {
                DijkstraNode destinationNode = new DijkstraNode();
                for (DijkstraNode graphNode : graphNodes) {
                    if (graphNode.getNodeID() == edge.getDestinationID()) 
                    destinationNode = graphNode;
                }
                NeighborNode sourceNeighborNode = new NeighborNode(destinationNode, edge.getDistanceWeight(), edge.getSimilarityWeight());
                NeighborNode destinationNeighborNode = new NeighborNode(node, edge.getDistanceWeight(), edge.getSimilarityWeight());
                node.addDestination(sourceNeighborNode);
                if (!destinationNode.getNeighborNodes().contains(destinationNeighborNode)) 
                    destinationNode.addDestination(destinationNeighborNode);
            }
        }

        // Since each node needs to finish their full circle of adding-neighbors-process, it needs its own loop
        for (DijkstraNode node : graphNodes) graph.addNode(node);

        // System.out.println(graph.getNodes().size());

        // apply Dijkstra to the graph
        graph = new Dijkstra().calculateShortestPathFromSource(graph, graph.getNodeByNodeID(nodeID));
        return graph;
    }

    /**
     * Using union-find algorithm to find connected components (subsets/disjoint sets)
     * @return List<ConnectedComponenet>
     * @throws IOException
     * @throws InterruptedException
     */
    public List<ConnectedComponenet> fetchConnectedComponents(List<WeightedNode> nearestNodeModels, DisjointUnionSets disjointUnionSets) throws IOException {

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
        // Check if all the neighbors are connected in one set -- not neccessary but make sure uninon-find works correctly
        //System.out.println(checkConnectivity(disjointUnionSets, connectedComponenets));
        return connectedComponenets;
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
