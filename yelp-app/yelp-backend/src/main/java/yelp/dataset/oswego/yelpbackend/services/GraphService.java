package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.io.IOException;
import yelp.dataset.oswego.yelpbackend.algorithms.dijkstra.Dijkstra;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraGraph;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraNode;
import yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set.DisjointUnionSets;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.NeighborNode;


/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

public class GraphService {

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
     * Initialize weighted graph
     * @param nodeID
     * @return
     * @throws IOException
     */
    public DijkstraGraph initGraph(int nodeID) throws IOException {
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);

        List<ConnectedComponenet> connectedComponenets = new GraphService().fetchConnectedComponents(nearestNodeModels, disjointUnionSets);

        // get specific disjoint set
        ConnectedComponenet connectedComponent = new GraphService().getConnectedComponent(connectedComponenets, disjointUnionSets.findDisjointSet(nodeID));

        // a list of all the nodes prepare for the graph for dijkstra
        List<DijkstraNode> graphNodes = new ArrayList<>();

        // Fill up graphNodes with new nodes using connectedNodeID
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
        
        return graph;
    }

        /**
     * Function to set up graph to run dijkstra
     * @param nodeID
     * @return
     * @throws IOException
     */
    public DijkstraGraph getDijkstraGraph(int nodeID) throws IOException {
        DijkstraGraph graph = initGraph(nodeID);
        // apply Dijkstra to the graph
        graph = new Dijkstra().calculateShortestPathFromSource(graph, graph.getNodeByNodeID(nodeID));
        return graph;
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

}
