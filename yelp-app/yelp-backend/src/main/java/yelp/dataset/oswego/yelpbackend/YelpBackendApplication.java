package yelp.dataset.oswego.yelpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@SpringBootApplication
@Component
public class YelpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(YelpBackendApplication.class, args);
	}

}
