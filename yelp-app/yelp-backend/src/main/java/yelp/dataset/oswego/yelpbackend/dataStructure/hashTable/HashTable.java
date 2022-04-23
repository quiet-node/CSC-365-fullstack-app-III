package yelp.dataset.oswego.yelpbackend.dataStructure.hashTable;

import java.util.HashMap;
import java.util.LinkedList;


/* 
    DESCRIPTION: Hash Table is an array of LinkedList, and each node::LinkedList is an instance of a hashmap contains key:frequency.
*/
public class HashTable {
    /* 
    - capacity indicates how big the table is, how many items the table can contain
    - size is the number of elements in the hashtable
    - threshold tells the table when to resize
    */
    private int capacity, threshold, size = 0; 
    private double loadFactor = 0.75;
    private LinkedList<HashMap<String, Integer>>[] table; // table is an array of Node 

    // Constructor
    public HashTable(int capacity)  {
        this.capacity = capacity; 
        threshold = (int) (this.capacity*loadFactor);
        table = new LinkedList[this.capacity];  
    }

    // unhashedIndex() normalizes the HashCode to find the index for each bucket
    private int unhashedIndex(int hashedIndex) {
        return (hashedIndex & 0x7FFFFFFF) & (this.capacity - 1); // 0x7FFFFFFF strips off the sign bit makes it always positive
    }


    // add() use to insert each term into the hashtable
    public void add(String term) {

        // find  the bucketIdex it belongs to
        int bucketIndex = unhashedIndex(term.hashCode()); 

        // termMap = { term: timesUsed }
        HashMap<String, Integer> termMap = new HashMap<>();

        // bucket:HashMap at table[bucketIndex]
        LinkedList<HashMap<String, Integer>> bucket = table[bucketIndex];

        
        if (bucket == null) { // if bucket is null means no item then add the hashMap
            table[bucketIndex] = bucket = new LinkedList<HashMap<String, Integer>>(); // fix NullPointer 
            termMap.put(term, 1); // build termMap
            bucket.add(termMap);
        } else { //collision happened
            // loop through bucket
            for(HashMap<String, Integer> collision : bucket) { //each collision is a hashmap
                for(String keyTerm : collision.keySet()) {  
                    if (keyTerm.equals(term)) { // if term is already there, increase the value
                        collision.put(keyTerm, collision.get(keyTerm) + 1);
                        return; // end else;
                    } 
                }
            } 
            // if term is not defined, then add new termMap
            termMap.put(term, 1);
            bucket.add(termMap);

        }

        ++size; // increment size

        if (size > threshold) resize(); // resize if threshold is reached

    };

    // resizes the table
    private void resize() {
        capacity = table.length << 1; // new capacity left shift 1 == 2x
        threshold = (int) (capacity * loadFactor);  //new threshold

        // newTable
        LinkedList<HashMap<String, Integer>>[] newTable = new LinkedList[capacity];
        

        // loop through table 
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                // each index on the table is a linkedlist of hashmap
                for(HashMap<String, Integer> collision : table[i]) {
                    
                    // get bucketIndex
                    int bucketIndex = -1; // -1 because incase keyTerm is not found. Safety purpose
                    for(String keyTerm : collision.keySet()){
                        bucketIndex = unhashedIndex(keyTerm.hashCode());
                    }
                    if (bucketIndex >= 0) { // keyTerm is found
                        LinkedList<HashMap<String, Integer>> bucket = newTable[bucketIndex];

                        if (bucket == null) { // if bucket is null means no item then add the hashMap
                            newTable[bucketIndex] = bucket = new LinkedList<HashMap<String, Integer>>(); // fix NullPointer 
                        } 
                        bucket.add(collision);
                    } else {
                        System.out.println("Something wrong with bucketIndex");
                    }

                } // finished adding all the business from old table to new table
            }
        }

        table = newTable; 

    }


    // check if bucket has term
    public boolean contains(String term) {
        int bucketIndex = unhashedIndex(term.hashCode()); // get bucketID
        LinkedList<HashMap<String, Integer>> bucket = table[bucketIndex]; // get bucket at bucketID

        // loop through the bucket to find if it contains term
        for (HashMap<String, Integer> collision : bucket) {
            for(String keyTerm : collision.keySet()) { // each collision is a hashmap
                if (term.equals(keyTerm)) {
                    return true;
                } 
            }
        }
        return false; // if no bucket contains term
    }

    // getTerm:int return term frequency
    public int getTerm(String term) {
        
        // find  the bucketIdex it belongs to
        int bucketIndex = unhashedIndex(term.hashCode()); 

        // bucket:HashMap at table[bucketIndex]
        LinkedList<HashMap<String, Integer>> bucket = table[bucketIndex];

        // loop through the bucket to find if it contains term
        if (bucket!=null) {
            for (HashMap<String, Integer> collision : bucket) {
                for(String keyTerm : collision.keySet()) { // each collision is a hashmap
                    if (term.equals(keyTerm)) {
                        return collision.get(term);
                    } 
                }
            }

        }

        // if term is not in the table
        return -1;
    }

    

}
