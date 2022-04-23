package yelp.dataset.oswego.yelpbackend.dataStructure.btree;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import yelp.dataset.oswego.yelpbackend.models.BusinessModel;

/** 
*   POJO will be wiped off by garbge collector after a running session
*   Needs to store on disk for disk => transfrom POJO into byte stream
*   java.io.Serializable does such job
*/
@Getter
public class BusinessBtree implements Serializable{
    private BusinessBNode root;
    private int minDeg; 

    public BusinessBtree(int minDeg) {
        this.root = null;
        this.minDeg = minDeg;
    }

 
    /**
     * An implementation for BusinessBNode::findKeyByBusinessID(int keyID).
     * A function to find a key using businessID
     * @param keyID
     * @return BusinessModel
     */
    public BusinessModel findKeyByBusinessID(int keyID) {
        if (this.root == null)
            return null;
        else
            return this.root.findKeyByBusinessID(keyID);
    }

    /**
     * A function to insert a new key to BusinessBTree.
     * Reference: https://www.geeksforgeeks.org/insert-operation-in-b-tree/
     * @param key (BusinessModel)
     */
    public void insert(BusinessModel key) {
        if (root == null) { // if tree is empty
            // init new root
            root = new BusinessBNode(this.minDeg, true);
            root.BKeys.set(0, key);
            root.BKeyNum = 1;
        } else { // tree is not empty
            // If root is full, then tree grows in height
            if (root.BKeyNum == (2 * minDeg - 1)) {
                
                // init new root
                BusinessBNode newRoot = new BusinessBNode(minDeg, false);

                // make old root as child of new root
                newRoot.BChild.set(0, root);

                // split the old root and move 1 key to the new root
                newRoot.splitChild(0, root);

                // New root has two children now. Decide which of the two children is going to have new key
                int index = 0;
                if (newRoot.BKeys.get(0).getId() < key.getId()) 
                    index++; 
                newRoot.BChild.get(index).addKey(key);

                // change root for the whole tree
                root = newRoot;
            }
            else { // if root is not full, addKey key
                root.addKey(key);
            }
        }
    }

    /**
     * An implementation for BusinessBNode::findRandomBusinesses(int amount).
     * A function to get a list of random businesses with the size of amount
     * @param amount
     * @return
     */
    public List<BusinessModel> findRandomBusinesses(int amount) {
        if (this.root == null)
            return null;
        else
            if (this.root.findRandomBusinesses(amount) == null) 
                return null;
            return this.root.findRandomBusinesses(amount);
    }

    /**
     * An implementation for BusinessBNode::findAll().
     * A function to get a list of all businessKey
     * @return
     */
    public List<BusinessModel> findAll() {
        if (this.root == null)
            return null;
        else
            if (this.root.findAll() == null) 
                return null;
            return this.root.findAll();
    }

}
