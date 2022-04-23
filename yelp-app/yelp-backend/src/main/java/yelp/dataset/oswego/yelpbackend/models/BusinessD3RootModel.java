package yelp.dataset.oswego.yelpbackend.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessD3RootModel {
    private List<BusinessD3ChildrenModel> children;
    private String name = "Yelp Dataset";
}
