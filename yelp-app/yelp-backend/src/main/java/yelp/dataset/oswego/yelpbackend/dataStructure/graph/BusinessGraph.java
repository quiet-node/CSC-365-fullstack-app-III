package yelp.dataset.oswego.yelpbackend.dataStructure.graph;

import java.util.List;

public class BusinessGraph {
    List<BusinessGNode> nodes;

    void clearParents() {nodes.forEach(source -> source.setParent(null));}

    boolean canReach (BusinessGNode source, BusinessGNode destination) {
        clearParents();
        return source.canReach(destination);
    }
}
