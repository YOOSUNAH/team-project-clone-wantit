package io.dcns.wantitauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableScheduling
@SpringBootApplication
public class WantItAuctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(WantItAuctionApplication.class, args);
    }

}
