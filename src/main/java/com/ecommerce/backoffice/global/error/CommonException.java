package com.ecommerce.backoffice.global.error;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final CommonError commonError;

    public CommonException(CommonError commonError) {
        super(commonError.getMessage());
        this.commonError = commonError;
    }
}
