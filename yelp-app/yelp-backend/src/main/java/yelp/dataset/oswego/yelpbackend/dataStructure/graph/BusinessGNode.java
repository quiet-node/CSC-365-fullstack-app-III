package yelp.dataset.oswego.yelpbackend.dataStructure.graph;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessGNode {
    int attribute;
    List<BusinessGNode> links;

    boolean canReach(BusinessGNode destination) {
        if (this == destination) return true;
        for (BusinessGNode node : links) 
            if (node.canReach(destination)) return true;
        return false;
    }
}
