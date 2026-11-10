package com.dymun.pricealert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PricealertApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricealertApplication.class, args);
	}

}
