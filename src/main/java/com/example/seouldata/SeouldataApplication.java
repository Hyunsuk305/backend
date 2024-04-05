package com.example.seouldata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example.seouldata.repository")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SeouldataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeouldataApplication.class, args);
	}

}
