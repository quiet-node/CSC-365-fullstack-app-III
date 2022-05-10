package yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class D3Link {
    private long source;
    private long target;
}
