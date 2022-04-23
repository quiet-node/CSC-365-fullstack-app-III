package yelp.dataset.oswego.yelpbackend.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessD3ChildrenModel {
    private String name;
    private List<BusinessD3Model> children;
}
