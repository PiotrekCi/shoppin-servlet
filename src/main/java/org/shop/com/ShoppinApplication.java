package org.shop.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ServletComponentScan
@EnableMongoRepositories
@ComponentScan
@EntityScan
public class ShoppinApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppinApplication.class, args);
    }
}
