package yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectedComponenet {
    private int root;
    private List<Integer> children;
}
