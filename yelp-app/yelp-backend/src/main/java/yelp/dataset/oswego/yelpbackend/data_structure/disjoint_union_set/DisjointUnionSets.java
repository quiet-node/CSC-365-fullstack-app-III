package yelp.dataset.oswego.yelpbackend.data_structure.disjoint_union_set;

import lombok.Data;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Data
public class DisjointUnionSets {
    private int[] parent, size;
    private int maxComponents;

    public DisjointUnionSets() {
        this.maxComponents = 10000;
        parent = new int[maxComponents];
        size = new int[maxComponents];;
        initializeDisjoinSet();
    }

    /**
     * Create maxComponenets sets with a single item in each set
     * Initially, all elements are in its own set 
     */
    private void initializeDisjoinSet() {
        for (int i = 0; i < maxComponents; i++) {
            parent[i]= i;
            size[i]= 1;
        };
    }

    /**
     * finds the representative of the set
     * @param targetNode
     * @return int - representative
     */
    public int findDisjointSet(int targetNode) {
        // if the targetNode is its own parent => the representative
        if (targetNode == parent[targetNode]) return targetNode;

        // if not, first compress the path then recursively claim up to find the root node => the representative
        return parent[targetNode] = findDisjointSet(parent[targetNode]);
    }

    /**
     * Union the unrelated nodes to form up disjoin sets
     * @param nodeA
     * @param nodeB
     */
    public void unionDisjoinSets(int nodeA, int nodeB) {
        nodeA = findDisjointSet(nodeA);
        nodeB = findDisjointSet(nodeB);

        if (nodeA == nodeB) return;

        if (nodeA != nodeB) {
            if (size[nodeA] > size[nodeB]) {
                parent[nodeB] = nodeA;
                size[nodeA] += 1;
            } else {
                parent[nodeA] = nodeB;
                size[nodeB] += 1;
            } 
        }
    }
}
