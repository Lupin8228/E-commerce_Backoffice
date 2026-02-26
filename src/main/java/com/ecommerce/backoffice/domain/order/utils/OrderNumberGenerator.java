package com.ecommerce.backoffice.domain.order.utils;

import com.github.f4b6a3.tsid.TsidCreator;
import org.springframework.stereotype.Component;

@Component
public class OrderNumberGenerator {
    public String generate() {

        // TSID 생성
        String tsid = TsidCreator.getTsid().toString();

        // prefix 붙이기
        return "ORD-" + tsid;
    }
}