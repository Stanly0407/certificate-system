package com.epam.esm.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.epam.esm.services", "com.epam.esm.repository", "com.epam.esm.configuration",
        "com.epam.esm.controllers", "com.epam.esm.entities"})
@EntityScan("com.epam.esm.entities")
public class SpringBootSecurityWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityWebApplication.class, args);
    }

}
