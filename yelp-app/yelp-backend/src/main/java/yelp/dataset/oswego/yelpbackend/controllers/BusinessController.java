package yelp.dataset.oswego.yelpbackend.controllers;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import yelp.dataset.oswego.yelpbackend.data_structure.dijkstra_graph.DijkstraGraph;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedNode;
import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;
import yelp.dataset.oswego.yelpbackend.models.d3_models.BusinessD3RootModel;
import yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components.ConnectedComponenet;
import yelp.dataset.oswego.yelpbackend.models.graph_models.node_models.NearestBusinessModel;
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

    @PostMapping("/graph/closest/four/edges/all/write-to-disk")
    public ResponseEntity<String> writeClosestFourByBusinessName() throws IOException{
        new GraphService().writeClosestFourToDataStore(10000);
        return new ResponseEntity<>("Successfully write to data store",HttpStatus.OK);
    }

    @GetMapping("/graph/closest/four/{requestedBusiness}")
    public ResponseEntity<NearestBusinessModel> getClosestFourByBusinessName(@PathVariable String requestedBusiness) throws IOException{
        List<BusinessModel> businesses = repo.findByName(requestedBusiness);
        if (businesses.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business Not Found");
        return new ResponseEntity<>(new RestService().getClosestFourByBusinessName(businesses.get(0)), HttpStatus.OK);
    }

    @GetMapping("/graph/closest/four/ID/{businessID}")
    public ResponseEntity<WeightedNode> getClosestFourByBusinessID(@PathVariable Long businessID) throws IOException{
        return new ResponseEntity<>(new RestService().getClosestFourByBusinessID(businessID), HttpStatus.OK);
    }

    @GetMapping("/graph/closest/four/edges/all")
    public ResponseEntity<List<WeightedNode>> getClosestFourEdgesForAll() throws IOException{
        return new ResponseEntity<>(new RestService().getClosestFourNodeList(), HttpStatus.OK);
    }

    @GetMapping("/graph/fetch/connected-components")
    public ResponseEntity<List<ConnectedComponenet>> connectivityCheck() throws IOException{
        return new ResponseEntity<>(new RestService().fetchConnectedComponents(), HttpStatus.OK);
    }
    @GetMapping("/graph/fetch/graph/{businessID}")
    public ResponseEntity<DijkstraGraph> getGraph(@PathVariable int businessID) throws IOException{
        return new ResponseEntity<>(new RestService().fetchGraph(businessID), HttpStatus.OK);
    }

}
