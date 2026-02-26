package com.ecommerce.backoffice.global.common.init;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.enums.CustomerStatus;
import com.ecommerce.backoffice.domain.customer.repository.CustomerRepository;
import net.datafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Random;

@Profile("local")
@Component
@RequiredArgsConstructor
public class CustomerDataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {

        // 이미 데이터 있으면 생성 안함
        if (customerRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(new Locale("ko"));
        Random random = new Random();

        for (int i = 0; i < 50; i++) {

            CustomerStatus status =
                    random.nextBoolean() ? CustomerStatus.ACTIVE : CustomerStatus.INACTIVE;

            Customer customer = Customer.builder()
                    .name(faker.name().fullName().replace(" ", ""))                    .email("customer" + (i+1) + "@test.com") // unique 보장
                    .phone(generatePhoneNumber(random))
                    .status(status)
                    .build();

            customerRepository.save(customer);
        }
    }

    private String generatePhoneNumber(Random random) {
        return "010-" +
                (1000 + random.nextInt(9000)) + "-" +
                (1000 + random.nextInt(9000));
    }
}