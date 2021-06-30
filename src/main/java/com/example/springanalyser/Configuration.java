package com.example.springanalyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.RequestScope;

@SpringBootApplication
public class Configuration {

    public static void main(String[] args) {
        SpringApplication.run(Configuration.class, args);
    }

    @Bean
    @RequestScope
    public User user() {
        return new User();
    }

}
