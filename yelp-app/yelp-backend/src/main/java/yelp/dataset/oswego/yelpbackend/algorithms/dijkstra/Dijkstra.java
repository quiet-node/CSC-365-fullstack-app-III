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
     * Main method to calculate shortest path based on a root node
     * @param graph
     * @param source
     * @return DijkstraGraph
     */
    public DijkstraGraph calculateShortestPathFromSource(DijkstraGraph graph, DijkstraNode source) {
        source.setWeight(0.0);
    
        List<DijkstraNode> visitedNodes = new ArrayList<>();
        List<DijkstraNode> unvisitedNodes = new ArrayList<>();
    
        unvisitedNodes.add(source);
    
        while (unvisitedNodes.size() != 0) {
            DijkstraNode currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);

            for (NeighborNode neighbor : currentNode.getNeighborNodes()){
                DijkstraNode neighborNode = neighbor.getNode();

                // Using inverse cosine similarity to get the weight
                // Let's say there are x and y like bellow
                // x = 0.23570226039551587 (more similar) => 1 - x = 0.7642977396 <lighter weight>
                // y = 0.10123880660581401 (less similar) => 1 - y = 0.89876119339 <heavier weight>
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
            double nodeDistance = node.getWeight();
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
        Double sourceDistance = sourceNode.getWeight();
        if (sourceDistance + edgeWeight < evaluationNode.getWeight()) {
            evaluationNode.setWeight(sourceDistance + edgeWeight);
            LinkedList<ShortestPath> shortestPaths = new LinkedList<>(sourceNode.getShortestPath());
            ShortestPath shortestPath = new ShortestPath(sourceNode.getNodeID(), sourceDistance + edgeWeight);
            shortestPaths.add(shortestPath);
            evaluationNode.setShortestPath(shortestPaths);
        }
    }
}
