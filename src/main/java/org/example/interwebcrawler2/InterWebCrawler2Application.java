package org.example.interwebcrawler2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class InterWebCrawler2Application {

    public static void main(String[] args) {
        SpringApplication.run(InterWebCrawler2Application.class, args);
    }
}
