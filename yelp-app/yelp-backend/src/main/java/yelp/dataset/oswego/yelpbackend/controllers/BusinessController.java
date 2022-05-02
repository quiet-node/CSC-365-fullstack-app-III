package yelp.dataset.oswego.yelpbackend.controllers;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedGraph;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.d3_models.BusinessD3RootModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.NearestBusinessModel;
import yelp.dataset.oswego.yelpbackend.repositories.BusinessRepository;
import yelp.dataset.oswego.yelpbackend.services.GraphService;
import yelp.dataset.oswego.yelpbackend.services.RestService;

@RestController
@RequestMapping("/yelpdata")
@CrossOrigin
public class BusinessController {
    @Autowired
    private BusinessRepository repo; // repo to store data

    @GetMapping("/get-all-businesses")
    public ResponseEntity<List<BusinessModel>> getAllBusinesses() {
        List<BusinessModel> allBusinesses = repo.findAll();
        if (allBusinesses == null) 
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return new ResponseEntity<>(allBusinesses, HttpStatus.OK);
    }

    @GetMapping("/{businessName}")
    public ResponseEntity<List<BusinessModel>> getBusinessByName(@PathVariable String businessName) {
        List<BusinessModel> business = repo.findByName(businessName);
        if (business == null) 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        return new ResponseEntity<>(business, HttpStatus.OK);
    }

    @GetMapping("/similar/{businessName}")
    public ResponseEntity<List<BusinessModel>> getSimilarBusinesses(@PathVariable String businessName) {
        List<BusinessModel> allBusinesses = repo.findAll();
        if (allBusinesses == null) 
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        
        BusinessModel targetB = repo.findByName(businessName).get(0);
        List<BusinessModel> similarBusinesses = new RestService().getSimilarBusinesses(allBusinesses, targetB);

        return  new ResponseEntity<>(similarBusinesses, HttpStatus.OK);
    }

    @GetMapping("/fetch-random-clusters")
    public ResponseEntity<Map<String, List<BusinessModel>>> fetchRandomCluster() throws IOException {
        return new ResponseEntity<>(new RestService().fetchClusters(), HttpStatus.OK);
    }

    @GetMapping("/fetch-d3-clusters")
    public ResponseEntity<BusinessD3RootModel> prepareD3() throws IOException {
        return new ResponseEntity<>(new RestService().prepareD3(), HttpStatus.OK);
    }
    
    @GetMapping("/{requestedBusiness}/closest/four")
    public ResponseEntity<List<BusinessModel>> getClosestFour(@PathVariable String requestedBusiness) throws IOException{
        List<BusinessModel> businesses = repo.findByName(requestedBusiness);
        if (businesses.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business Not Found");
        BusinessModel requestedBusinessModel = businesses.get(0);
        List<BusinessModel> businessList = new RestService().getClosestFour(requestedBusinessModel);
        businessList.add(0, requestedBusinessModel);
        return new ResponseEntity<>(businessList, HttpStatus.OK);
    }

    @GetMapping("/{requestedBusiness}/closest/four/graph")
    public ResponseEntity<WeightedGraph> getClosestFourGraph(@PathVariable String requestedBusiness) throws IOException{
        List<BusinessModel> businesses = repo.findByName(requestedBusiness);
        if (businesses.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business Not Found");
        BusinessModel requestedBusinessModel = businesses.get(0);
        List<BusinessModel> requestedBusinessList = new RestService().getClosestFour(requestedBusinessModel);

        return new ResponseEntity<>(new WeightedGraph(requestedBusinessModel, requestedBusinessList), HttpStatus.OK);
    }

    @GetMapping("/closest/four/hash-map")
    public ResponseEntity<List<NearestBusinessModel>> getClosestFourHashMap() throws IOException{
        return new ResponseEntity<>(new GraphService().getClosestFourHashMap(10000), HttpStatus.OK);
    }
}
