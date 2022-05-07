package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraGraph;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraNode;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.NeighborNode;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;

@Data
public class Dijkstra {

    /**
     * Main method to calculate shortest path
     * @param graph
     * @param source
     * @return DijkstraGraph
     */
    public DijkstraGraph calculateShortestPathFromSource(DijkstraGraph graph, DijkstraNode source) {
        source.setDistance(0.0);
    
        List<DijkstraNode> visitedNodes = new ArrayList<>();
        List<DijkstraNode> unvisitedNodes = new ArrayList<>();
    
        unvisitedNodes.add(source);

        // x = 0.23570226039551587 => 1 - x = 0.7642977396

        // y = 0.10123880660581401 => 1 - y = 0.89876119339
    
        while (unvisitedNodes.size() != 0) {
            DijkstraNode currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);

            for (NeighborNode neighbor : currentNode.getNeighborNodes()){
                DijkstraNode neighborNode = neighbor.getNode();
                Double similarityWeight = 1 - neighbor.getSimilarityWeight();
                
                if (!visitedNodes.contains(neighborNode)) {
                    calculateMinimumDistance(neighborNode, similarityWeight, currentNode);
                    unvisitedNodes.add(neighborNode);
                }
            }
            visitedNodes.add(currentNode);
        }
        return graph;
    }

    /**
     * Helper function 1
     * @param unsettledNodes
     * @return
     */
    private static DijkstraNode getLowestDistanceNode(List<DijkstraNode> unsettledNodes) {
        DijkstraNode lowestDistanceNode = null;
        double lowestDistance = Double.MAX_VALUE;
        for (DijkstraNode node: unsettledNodes) {
            double nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    /**
     * Helper function 2
     * @param evaluationNode
     * @param edgeWeight
     * @param sourceNode
     */
    private static void calculateMinimumDistance(DijkstraNode evaluationNode, Double edgeWeight, DijkstraNode sourceNode) {
        Double sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            LinkedList<ShortestPath> shortestPaths = new LinkedList<>(sourceNode.getShortestPath());
            ShortestPath shortestPath = new ShortestPath(sourceNode.getNodeID(), sourceDistance + edgeWeight);
            shortestPaths.add(shortestPath);
            evaluationNode.setShortestPath(shortestPaths);
        }
    }
}
