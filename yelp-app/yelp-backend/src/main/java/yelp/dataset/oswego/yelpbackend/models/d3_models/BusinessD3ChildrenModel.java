package yelp.dataset.oswego.yelpbackend.models.d3_models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@Data
@NoArgsConstructor
public class BusinessD3ChildrenModel {
    private String name;
    private List<BusinessD3Model> children;
}
