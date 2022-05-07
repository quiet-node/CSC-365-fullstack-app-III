package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;
import java.util.Map.Entry;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraGraph;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraNode;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;

@Data
public class Dijkstra {

    public DijkstraGraph calculateShortestPathFromSource(DijkstraGraph graph, DijkstraNode source) {
        source.setDistance(0.0);
    
        List<DijkstraNode> settledNodes = new ArrayList<>();
        List<DijkstraNode> unsettledNodes = new ArrayList<>();
    
        unsettledNodes.add(source);
    
        while (unsettledNodes.size() != 0) {
            DijkstraNode currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Entry < DijkstraNode, Double> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                DijkstraNode adjacentNode = adjacencyPair.getKey();
                Double edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

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
