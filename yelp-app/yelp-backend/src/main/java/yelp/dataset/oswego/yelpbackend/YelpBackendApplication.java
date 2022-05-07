package yelp.dataset.oswego.yelpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * @author: Nam (Logan) Nguyen
 * @college: SUNY Oswego
 * @since Spring 2022
 * @version 3.0
 * @link: https://github.com/lgad31vn/CSC-365
 */

@SpringBootApplication
@Component
public class YelpBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(YelpBackendApplication.class, args);
	}

}
