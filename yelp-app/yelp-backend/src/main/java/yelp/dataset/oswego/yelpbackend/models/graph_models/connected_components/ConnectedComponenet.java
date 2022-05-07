package yelp.dataset.oswego.yelpbackend.models.graph_models.connected_components;

import java.util.List;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class ConnectedComponenet {
    private int rootID;
    private List<Integer> children;
}
