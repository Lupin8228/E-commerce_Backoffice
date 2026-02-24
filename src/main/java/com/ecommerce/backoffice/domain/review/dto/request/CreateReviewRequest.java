package com.ecommerce.backoffice.domain.review.dto.request;

import jakarta.validation.constraints.*;

public record CreateReviewRequest(
        @NotNull(message = "평점은 필수 입력 항목입니다.")
        @Min(value = 1, message = "1에서 5사이의 값만 입력해주세요.")
        @Max(value = 5, message = "1에서 5사이의 값만 입력해주세요.")
        Integer rating,

        @NotBlank(message = "리뷰는 필수 입력 항목입니다.")
        @Size(min = 10, message = "리뷰는 10자 이상 입력해주세요.")
        String description
) {
}
