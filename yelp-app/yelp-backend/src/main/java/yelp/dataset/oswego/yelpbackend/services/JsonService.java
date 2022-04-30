package yelp.dataset.oswego.yelpbackend.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import org.json.JSONObject;

import lombok.Getter;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.models.businessModels.BusinessModel;

@Getter
public class JsonService{
    BusinessBtree businessBtree = new BusinessBtree(64);

    /**
     * Initialize a B-tree
     * @param PATH the path to the file
     * @return BusinessBtree
     */
    public BusinessBtree initBusinessBtree(String PATH) {
        try {
            // buffrer reader to read lines in json file
            FileReader reader = new FileReader(PATH);
            BufferedReader br = new BufferedReader(reader);
            String line = "";

            // loop through the json file
            for (int i = 0; i < 10000; i++) {
                // each line of the file is a json object
                line = br.readLine();
    
                // this is the whole Object for the whole line
                JSONObject bData = new JSONObject(line); 
                
                // attributes
                String name = bData.get("name").toString();
                String business_id = bData.get("business_id").toString();
                String address = bData.get("address").toString();
                Double stars = bData.getDouble("stars");
                Double reviews = bData.getDouble("review_count");
                Double longitude = bData.getDouble("longitude");
                Double latitude = bData.getDouble("latitude");
                Double similarityRate = -9999.0;

                // A list of string for categories
                ArrayList<String> bCategories = new ArrayList<String>();

                // get the values for categories-key and push them into an array
                String[] categories = bData.get("categories").toString().split(",");

                // add each category value to bCategories list
                for (int j =0; j<categories.length; j++) {
                    bCategories.add(categories[j].trim());
                }

                // Init a BusinessModel instance
                BusinessModel bModel = new BusinessModel(i, business_id, name, address, stars, reviews, similarityRate, longitude, latitude, bCategories);
                
                // add to businessBrree
                businessBtree.insert(bModel);

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return businessBtree;
    }

}
