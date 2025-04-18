package io.dcns.wantitauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableCaching
@EnableWebSecurity
@EnableScheduling
@SpringBootApplication
public class WantItAuctionApplication {
    public static void main(String[] args) {
        SpringApplication.run(WantItAuctionApplication.class, args);
    }
}
