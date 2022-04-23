package yelp.dataset.oswego.yelpbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yelp.dataset.oswego.yelpbackend.models.BusinessModel;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessModel, Long>  {
    List<BusinessModel> findByName(String name);
    BusinessModel findById(int id);
}
