package yelp.dataset.oswego.yelpbackend.dataStructure.graph;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessGNode {
    private int attribute;
    private List<BusinessGNode> links;
    private BusinessGNode parent; 

    public BusinessGNode(int attribute) {
        this.attribute = attribute;
        links = null;
    }

    /**
     * Reachability check
     * @param destination
     * @return 
     */
    public boolean canReach(BusinessGNode destination) {
        if (this.attribute == destination.attribute) return true;
        if (this.parent != null) return false;
        for (BusinessGNode bgNode : links) {
            bgNode.parent = this; // set parent be this node
            if (bgNode.canReach(destination)) return true;
        }
        return false;
    }

    
}
