package yelp.dataset.oswego.yelpbackend.models.centroid_models;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Centroid {
    private final long id;
    private final String businessName;
    private final ArrayList<String> categories;
}
