package yelp.dataset.oswego.yelpbackend.models.graph_models.rd3g_models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class D3GraphModel {
    private List<D3NodeModel> nodes;
    private List<D3LinkModel> links;
}