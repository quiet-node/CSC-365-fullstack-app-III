package yelp.dataset.oswego.yelpbackend.algorithms.dijkstra;

import java.util.*;
import java.util.Map.Entry;

import lombok.Data;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.Graph;
import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.Node;
import yelp.dataset.oswego.yelpbackend.models.graph_models.dijkstra_models.ShortestPath;

@Data
public class Dijkstra {

    public Graph calculateShortestPathFromSource(Graph graph, Node source) {
        source.setDistance(0.0);
    
        List<Node> settledNodes = new ArrayList<>();
        List<Node> unsettledNodes = new ArrayList<>();
    
        unsettledNodes.add(source);
    
        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Entry < Node, Double> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
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

    private static Node getLowestDistanceNode(List<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        double lowestDistance = Double.MAX_VALUE;
        for (Node node: unsettledNodes) {
            double nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode, Double edgeWeight, Node sourceNode) {
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
