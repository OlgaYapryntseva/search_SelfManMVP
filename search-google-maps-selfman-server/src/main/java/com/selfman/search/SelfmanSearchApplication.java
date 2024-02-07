package com.selfman.search;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO: add search by filters (e.g., country, city, reviews, industry)
//TODO: consider localization issue
//TODO: add Docker
@OpenAPIDefinition(info = @Info(
        title = "SelfMan Search",
        version = "1.0.0",
        description = "SelfMan Platform Search Server"))
@SpringBootApplication
public class SelfmanSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SelfmanSearchApplication.class, args);
    }

}
