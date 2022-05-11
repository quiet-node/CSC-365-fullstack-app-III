package yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class D3GraphModel {
    private Set<D3NodeModel> nodes;
    private Set<D3LinkModel> links;
}
