package com.cerner.shipit.DynaSites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class DynaSitesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynaSitesApplication.class, args);
	}

}

