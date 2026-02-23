package com.ecommerce.backoffice.domain.product.config;

import com.ecommerce.backoffice.domain.product.enums.ProductCategory;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // ProductStatus 변환기
        registry.addConverter(new Converter<String, ProductStatus>() {
            @Override
            public ProductStatus convert(String source) {
                return ProductStatus.fromValue(source);
            }
        });

        // ProductCategory 변환기
        registry.addConverter(new Converter<String, ProductCategory>() {
            @Override
            public ProductCategory convert(String source) {
                return ProductCategory.fromValue(source);
            }
        });
    }
}
