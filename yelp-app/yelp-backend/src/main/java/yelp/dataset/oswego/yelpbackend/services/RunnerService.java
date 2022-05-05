package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;

@Component // this + CommandLineRunner are used to run code at application startup
public class RunnerService implements CommandLineRunner{
    
    @Override
    public void run(String... args) throws Exception {

        /**
         * This file here is show how to read and write files to disk
         * RUN IT ONCE THEN SCRAP IT
         */

        // /** READING/WRITING BTREE TO DISK */
        // new IOService().writeBtree(new JsonService().initBusinessBtree("{SYSTEM_PATH}/yelp-app/yelp-dataset/business.json"));
        // BusinessBtree businessBtree = new IOService().readBtree();

       
    }
}

