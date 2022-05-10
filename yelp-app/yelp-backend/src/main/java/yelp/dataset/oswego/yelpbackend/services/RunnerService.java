package yelp.dataset.oswego.yelpbackend.services;

import java.util.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Component // this + CommandLineRunner are used to run code at application startup
public class RunnerService implements CommandLineRunner{
    
    @Override
    public void run(String... args) throws Exception {
        /**
         * This file here is show how to read and write files to disk
         * RUN IT ONCE THEN SCRAP IT
         */

        // /** READING/WRITING BTREE TO DISK */
        // BusinessBtree businessBtree = new JsonService().initBusinessBtree("{SOURCE-PATH}/yelp-app/yelp-dataset/business.json");
        // new IOService().writeBtree(businessBtree);
        // BusinessBtree businessBtree = new IOService().readBtree();
        // new JsonService().initBusinessBtree("{SOURCE-PATH}/yelp-app/yelp-dataset/business.json");
    }
}

