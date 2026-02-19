package com.ecommerce.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ECommerceBackofficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceBackofficeApplication.class, args);
    }

}
