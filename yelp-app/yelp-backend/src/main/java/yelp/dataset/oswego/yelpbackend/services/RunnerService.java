package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedGraph;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.NearestBusinessModel;

@Component // this + CommandLineRunner are used to run code at application startup
public class RunnerService implements CommandLineRunner{
    
    @Override
    public void run(String... args) throws Exception {

    /**
     * This file here is to keep in mind how to read and write files to disk
     * RUN IT ONCE THEN SCRAP IT
     */

        /** READING/WRITING BTREE TO DISK */
        new IOService().writeBtree(new JsonService().initBusinessBtree("{SYSTEM_PATH}/yelp-app/yelp-dataset/business.json"));
        BusinessBtree businessBtree = new IOService().readBtree();

        // /** READING/WRITING 10000 NODES WITH EDGES TO DISK */
        List<NearestBusinessModel> closestFourList = new GraphService().getClosestFour(10000);
        for (NearestBusinessModel nearestBusinessModel : closestFourList) {
            new IOService().writeNodesWithEdges(nearestBusinessModel);
        }
    }
}

