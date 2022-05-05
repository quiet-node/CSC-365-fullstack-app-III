package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;
import java.io.IOException;

import yelp.dataset.oswego.yelpbackend.algorithms.haversine.Haversine;
import yelp.dataset.oswego.yelpbackend.algorithms.similarity.CosSim;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set.DisjointUnionSets;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModelComparator;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestBusinessModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestNodeModel;

public class GraphService {

    /**
     * Find four geographically nearest businesses for each business and write it to disk
     * @param numberOfNodes
     * @return List<NearestBusinessModel>
     * @throws IOException
     */
    public List<NearestNodeModel> writeClosestFourToDataStore(int numberOfNodes) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        List<NearestNodeModel> nearestNodesList = new ArrayList<>();
        
        for (int i = 0; i < numberOfNodes; i++) {
            List<BusinessModel> closestFourBusinessModelList = new ArrayList<>();
            List<WeightedEdge> edges = new ArrayList<>();
            BusinessModel targetBusiness = businessBtree.findKeyByBusinessID(i);
            for (int j = 0; j < numberOfNodes; j++) {
                BusinessModel comparedBusiness = businessBtree.findKeyByBusinessID(j);
                if (targetBusiness.getId() != comparedBusiness.getId()){
                    double distanceWeight = new Haversine().calculateHaversine(targetBusiness, comparedBusiness);
                    comparedBusiness.setDistance(distanceWeight);
                    closestFourBusinessModelList.add(comparedBusiness);
                }
            }
            Collections.sort(closestFourBusinessModelList, new BusinessModelComparator());
            closestFourBusinessModelList = closestFourBusinessModelList.subList(0, 4);
            for (BusinessModel businessModel : closestFourBusinessModelList) {
                double simRateWeight = new CosSim().calcSimRate(targetBusiness.getCategories(), businessModel.getCategories());
                WeightedEdge weightedEdge = new WeightedEdge(targetBusiness.getId(), businessModel.getId(), businessModel.getDistance(), simRateWeight);
                edges.add(weightedEdge);
            }
            nearestNodesList.add(new NearestNodeModel(targetBusiness.getId(), edges));
            
            // write each node to disk -- this takes 4 minutes and 2.64 seconds to finish
            // new IOService().writeNodesWithEdges(nearestNodesList.get(i)); 
        }
        // write whole list to disk -- this takes 24.09 seconds to finish
        // new IOService().writeNearestNodesList(nearestNodesList);
        return nearestNodesList;
    }


    /**
     * Find four geographically nearest businesses based on business name
     * @param requestedBusinessModel
     * @return List<BusinessModel>
     * @throws IOException
     */
    public NearestBusinessModel getClosestFourByBusinessName(BusinessModel requestedBusinessModel) throws IOException {
        BusinessBtree businessBtree = new IOService().readBtree();
        NearestNodeModel nearestNodeModel = new IOService().readNodesWithEdges(requestedBusinessModel.getId());
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
     */
    public List<ConnectedComponenet> fetchConnectedComponents() throws IOException {
        List<NearestNodeModel> nearestNodeModels = new IOService().readNearestNodesList();
        DisjointUnionSets disjointUnionSets = new DisjointUnionSets();

        nearestNodeModels.forEach(model -> {
            model.getEdges().forEach(edge -> {
                System.out.println(edge);
                int sourceRoot = disjointUnionSets.findDisjointSet((int) edge.getSourceID());
                int destinationRoot = disjointUnionSets.findDisjointSet((int) edge.getDestinationID());
                disjointUnionSets.unionDisjoinSets(sourceRoot, destinationRoot);
            });
        });

        List<Integer> rootSet = new ArrayList<>(new HashSet<Integer>(disjointUnionSets.getParent()));

        List<ConnectedComponenet> connectedComponenets = new ArrayList<>();
        for (int i = 0; i < rootSet.size(); i++) {
            int root = rootSet.get(i);
            List<Integer> children = new ArrayList<>();
                for (int j = 0; j < disjointUnionSets.getParent().size(); j++) {
                    if (disjointUnionSets.getParent().get(j) == root) {
                        children.add(j);
                    }
                }
            connectedComponenets.add(new ConnectedComponenet(root, children));
        }
        return connectedComponenets;
    }
}
