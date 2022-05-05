package yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class DisjointUnionSets {
    private List<Integer> parent, size;
    private int maxComponents;

    public DisjointUnionSets() {
        parent = new ArrayList<>(Collections.nCopies(maxComponents, 0));
        size = new ArrayList<>(Collections.nCopies(maxComponents, 0));
        this.maxComponents = 10000;
        initializeDisjoinSet();
    }

    /**
     * Create maxComponenets sets with a single item in each set
     * Initially, all elements are in its own set 
     */
    private void initializeDisjoinSet() {
        for (int i = 0; i < maxComponents; i++) {
            parent.set(i, i);
            size.set(i, 1);
        };
    }

    /**
     * finds the representative of the set
     * @param targetNode
     * @return int - representative
     */
    public int findDisjointSet(int targetNode) {
        // if the targetNode is its own parent => the representative
        if (targetNode == parent.get(targetNode)) return targetNode;

        // if not, first compress the path then recursively claim up to find the root node => the representative
        return parent.set(targetNode, findDisjointSet(parent.get(targetNode)));
    }

    /**
     * 
     * @param nodeA
     * @param nodeB
     */
    public void unionDisjoinSets(int nodeA, int nodeB) {
        nodeA = findDisjointSet(nodeA);
        nodeB = findDisjointSet(nodeB);

        if (nodeA == nodeB) return;

        if (nodeA != nodeB) {
            if (size.get(nodeA) < size.get(nodeB)) {
                parent.set(nodeA, nodeB);
                size.set(nodeB, size.get(nodeB) + 1);
            } else if (size.get(nodeA) > size.get(nodeB) ){
                parent.set(nodeB, nodeA);
                size.set(nodeA, size.get(nodeA) + 1);
            } else {
                if (nodeA < nodeB) {
                    parent.set(nodeA, nodeB);
                    size.set(nodeB, size.get(nodeB) + 1);
                } else {
                    parent.set(nodeB, nodeA);
                    size.set(nodeA, size.get(nodeA) + 1);
                }
            }
        }
    }
}
