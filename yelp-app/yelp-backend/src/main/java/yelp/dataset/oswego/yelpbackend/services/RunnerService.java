package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.util.Map.Entry;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set.DisjointUnionSets;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;

@Component // this + CommandLineRunner are used to run code at application startup
public class RunnerService implements CommandLineRunner{

    @Data
    public class Graph {

        private List<Node> nodes = new ArrayList<>();
    
        public void addNode(Node node) {nodes.add(node);};

        public List<Node> getNodes() {
            return nodes;
        }
    
        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public Node getNodeByNodeID(long nodeID) {
            for(Node node : nodes) 
                if (node.getNodeID() == nodeID) 
                    return node;
            return null;
        }
    }

    public class Node {

        private Long nodeID;
        private List<Node> shortestPath = new ArrayList<>();
        private Double distance = Double.MAX_VALUE;
        Map<Node, Double> adjacentNodes = new HashMap<>();
        // private List<WeightedEdge> edges;
            
        public Node (){};
        public Node(long sourceID) {
            this.nodeID = sourceID;
        }

        public void addDestination(Node destination, double distance) {
            adjacentNodes.put(destination, distance);
        }
    
        public Long getNodeID() {
            return nodeID;
        }
    
        public void setNodeID(Long name) {
            this.nodeID = name;
        }
    
        public Map<Node, Double> getAdjacentNodes() {
            return adjacentNodes;
        }
    
        public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
            this.adjacentNodes = adjacentNodes;
        }
    
        public Double getDistance() {
            return distance;
        }
    
        public void setDistance(Double distance) {
            this.distance = distance;
        }
    
        public List<Node> getShortestPath() {
            return shortestPath;
        }
    
        public void setShortestPath(LinkedList<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }

        @Override
        public String toString() {
            // return "Node: "+name+", distance: " + distance +", shortest path: " +shortestPath + ", neighbors: "+ adjacentNodes.toString();
            return "Node: "+nodeID+", distance: " + distance +", shortest path: " +shortestPath;
        }
    
    }

    @Data
    public class Dijkstra {

        public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
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
                    if (adjacentNode.getNodeID() == 602) 
                        System.out.println();
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
                LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
                shortestPath.add(sourceNode);
                evaluationNode.setShortestPath(shortestPath);
            }
        }
    }
    
    @Override
    public void run(String... args) throws Exception {
        /**
         * This file here is show how to read and write files to disk
         * RUN IT ONCE THEN SCRAP IT
         */

        // /** READING/WRITING BTREE TO DISK */
        // new IOService().writeBtree(new JsonService().initBusinessBtree("{SYSTEM_PATH}/yelp-app/yelp-dataset/business.json"));
        // BusinessBtree businessBtree = new IOService().readBtree();

        // Graph graph = new GraphService().setUpDijkstraGraph(9475);
        // Node source = graph.getNodeByNodeID(9475);
        // graph = new Dijkstra().calculateShortestPathFromSource(graph, new Node(9475));
        // System.out.println(source);

        Graph graph = new Graph();

        // Node node7437 = new Node(7437);
        // Node node1071 = new Node(1071);
        // Node node8115 = new Node(8115);
        // Node node5921 = new Node(5921); 
        // Node node8302 = new Node(8302);

        // node7437.addDestination(node1071, 0.1044659872900713);
        // node7437.addDestination(node8115, 0.3404450584030827);
        // node7437.addDestination(node5921, 0.3792118158825995);
        // node7437.addDestination(node8302, 0.4278822389789062);

        // node1071.addDestination(node7437, 0.1044659872900713);
        // node1071.addDestination(node8115, 0.23679440165923374);
        // node1071.addDestination(node5921, 0.27698394117256075);
        // node1071.addDestination(node8302, 0.3234346026652249);

        // node8115.addDestination(node5921, 0.04672857739911382);
        // node8115.addDestination(node8302, 0.09364284733726737);
        // node8115.addDestination(node1071, 0.23679440165923374);
        // node8115.addDestination(node7437, 0.3404450584030827);

        // node5921.addDestination(node8115, 0.04672857739911382);
        // node5921.addDestination(node8302, 0.08086399983591412);
        // node5921.addDestination(node1071, 0.27698394117256075);
        // node5921.addDestination(node7437, 0.3792118158825995);

        // node8302.addDestination(node5921, 0.08086399983591412);
        // node8302.addDestination(node8115, 0.09364284733726737);
        // node8302.addDestination(node1071, 0.3234346026652249);
        // node8302.addDestination(node7437, 0.4278822389789062);

        // graph.addNode(node7437);
        // graph.addNode(node1071);
        // graph.addNode(node8115);
        // graph.addNode(node5921);
        // graph.addNode(node8302);


        List<ConnectedComponenet> connectedComponenets = new GraphService().fetchConnectedComponents();
        List<WeightedNode> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new GraphService().setUpDisjoinSets(nearestNodeModels);

        ConnectedComponenet connectedComponent = new GraphService().getConnectedComponent(connectedComponenets, disjointUnionSets.findDisjointSet(6507));

        List<Node> graphNodes = new ArrayList<>();

        for (int connectedNodeID : connectedComponent.getChildren()) {
            System.out.print(connectedNodeID +", ");
            graphNodes.add(new Node(connectedNodeID));
        }
        System.out.println();

        for (Node node : graphNodes) {
            WeightedNode weightedNode = new IOService().readNodesWithEdges(node.getNodeID());
            for (WeightedEdge edge : weightedNode.getEdges()) {
                Node neighbor = new Node();
                for (Node graphNode : graphNodes) {
                    if (graphNode.getNodeID() == edge.getDestinationID()) 
                        neighbor = graphNode;
                }
                node.addDestination(neighbor, edge.getDistanceWeight());
            }
        }

        //     602, 
        //     752, v x
        //     1420, v x
        //     6507, 
        //     7027, v x
        //     9929 v x

        for (Node node : graphNodes) graph.addNode(node);

        Node source = graph.getNodeByNodeID(6507);
        // System.out.println(source.getAdjacentNodes().toString());
        
        // graph.getNodes().forEach(node -> {
        //     System.out.println(node);
        //     System.out.println(node.getAdjacentNodes().toString());
        //     System.out.println();
        // });


        System.out.println(disjointUnionSets.findDisjointSet(6507));
        System.out.println(disjointUnionSets.findDisjointSet(602));

        graph = Dijkstra.calculateShortestPathFromSource(graph, source);

        graph.getNodes().forEach(node -> {
            System.out.println();
            System.out.println("for node: " +node);
        });


    }
}

