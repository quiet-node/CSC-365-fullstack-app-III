package yelp.dataset.oswego.yelpbackend.dataStructure.graph;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessGNode {
    int attribute;
    List<BusinessGNode> links;

}
