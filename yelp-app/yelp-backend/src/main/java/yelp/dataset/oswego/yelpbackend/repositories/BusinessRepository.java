package yelp.dataset.oswego.yelpbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yelp.dataset.oswego.yelpbackend.models.business_models.BusinessModel;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Repository
public interface BusinessRepository extends JpaRepository<BusinessModel, Long>  {
    List<BusinessModel> findByName(String name);
    BusinessModel findById(int id);
}
