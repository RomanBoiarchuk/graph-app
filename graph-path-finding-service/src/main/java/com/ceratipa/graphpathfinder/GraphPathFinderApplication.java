package com.ceratipa.graphpathfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GraphPathFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphPathFinderApplication.class, args);
	}

}
