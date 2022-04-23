package yelp.dataset.oswego.yelpbackend.algorithms.similarity;

import java.util.ArrayList;
import java.util.HashSet;

import lombok.NoArgsConstructor;
import yelp.dataset.oswego.yelpbackend.dataStructure.hashTable.HashTable;

@NoArgsConstructor
public class CosSim {
    private double cosSimRate;

    /**
     * A function filters out the "&" and " " from the Yelp dataset
     * For example:
     *      catA = ["Gastropubs"," Food"," Beer Gardens"," Restaurants"," Bars"," American (Traditional)"," Beer Bar"," Nightlife"," Breweries"]
     *      catB = [Restaurant, Beauty & Spa,  Coffee & Tea, Hair Salons, Food]
     * Then catFilter return a HashSet
     *      vector =  [American, Store, Gardens, Coffee, Restaurants, Beer, Nightlife, Gastropubs, Tea, Convenience, Bar, Breweries, (Traditional), Food, Bars] 
     * @param catA
     * @param catB
     * @return HashSet termMatrix
     */
    private HashSet<String> catFilter(ArrayList<String> catA, ArrayList<String> catB) {
        HashSet<String> termMatrix = new HashSet<>();

        // filter catA
        for(String cat : catA) { // EX: cat = "Coffee & Tea"
            cat = cat.trim();
            String[] catArr = cat.split(" "); // catArr = ["Coffee", "&", "Tea"]

            for(String c : catArr) { // c = "Coffee", c="&", c="Tea"
                if (!c.equals("&")){
                    termMatrix.add(c);
                }
            }
        }

        // filter and add catB to termMatrix
        for (String cat : catB) {
            cat = cat.trim();
            String[] catArr = cat.split(" ");

            for(String c : catArr) {
                if (!c.equals("&")) {
                    termMatrix.add(c);
                }
            }
        }

        return termMatrix;
    }

    /**
     * A function to make a vector for each category
     * @param termMatrix
     * @param categories
     * @return HashTable vector
     */
    private HashTable makeVector(HashSet<String> termMatrix, ArrayList<String> categories) {
        // init a vector:hashtable
        HashTable vector = new HashTable(10);

        for (String term: termMatrix){  // loop through termMatrix
            term = term.trim(); // clean the term
            for(String cat : categories) {   // cat can be "Coffee & Tea"
               cat = cat.trim();
               String[] catArr = cat.split(" "); //catArr = ["Coffee", "&", "Tea"]
               for(String c:catArr) { // c = "Coffee", c ="&", c ="Tea"
                   if (term.equals(c)) { 
                        vector.add(term);
                   }

                }
            }
        }

        // Each vector is a hashtable
        return vector;
    }

    /**
     * A function to calculate dot product of two vectors
     * @param catA
     * @param catB
     * @return dot product
     */
    private double calcDotProduct(ArrayList<String> catA, ArrayList<String> catB) {
        // init dotProd:double
        double dotProd = 0;

        // termMatrix contains all relevant words from catA and catB
        HashSet<String> termMatrix = catFilter(catA, catB);

        // init vectors:HashTable
        HashTable vectorA = makeVector(termMatrix, catA);
        HashTable vectorB = makeVector(termMatrix, catB);
        // loop through termMatrix
        for(String term : termMatrix) {
            // dotProd = x1*y1 + x2*y2
            // It masters only when term != null ie. term.value > 0
            if (vectorA.getTerm(term) > 0 && vectorB.getTerm(term) > 0) {
                    int valueA = vectorA.getTerm(term);
                    int valueB = vectorB.getTerm(term);

                    int product = valueA * valueB;
                    dotProd += product; 
            }
        }
        return dotProd;        
    }

    /**
     * A function calculates the magnitude of each vector 
     * @param termMatrix
     * @param vector
     * @return vector's magnitude
     */
    private double calcMagnitude(HashSet<String> termMatrix, HashTable vector) {
        double sumFreq = 0.0;
        
        // loop through termMatrix
        for(String term : termMatrix) {
            //It masters only when term != null ie. term.value > 0
            if (vector.getTerm(term) > 0) {
                sumFreq += Math.pow(vector.getTerm(term), 2);
            }
        }

        return Math.sqrt(sumFreq);
    }


    /**
     * A function to calculate magnitude product of 2 vectors
     * @param catA
     * @param catB
     * @return magnitude product
     */
    private double calcMagProduct(ArrayList<String> catA, ArrayList<String> catB) {

        // termMatrix contains all relevant words from catA and catB
        HashSet<String> termMatrix = catFilter(catA, catB);

        // doctermMatrix:HashMap<String ,Integer> 
        HashTable vectorA = makeVector(termMatrix, catA);
        HashTable vectorB = makeVector(termMatrix, catB);

        double magVectorA = calcMagnitude(termMatrix, vectorA);
        double magVectorB = calcMagnitude(termMatrix, vectorB);


        return magVectorA * magVectorB;
        
    }


    // calculate simRate 
    /**
     * Main function to calculate the similarity rate between 2 categories using Cosine Similarity metric
     * @param categoriesA
     * @param categoriesB
     * @return similarity rate
     */
    public double calcSimRate(ArrayList<String> categoriesA, ArrayList<String> categoriesB) {
        /* 
        * Cos(X, Y) = (X.Y) / (||X|| * ||Y||)
        * Pseudo: simRate = Cos(X,Y) = (dotProduct) / (Magnitude vector X * Magnitude vector Y)
        */

        double dotProduct = calcDotProduct(categoriesA, categoriesB);
        double magProduct = calcMagProduct(categoriesA, categoriesB);

        this.cosSimRate = dotProduct / magProduct;

        return this.cosSimRate;
    }

  


}
