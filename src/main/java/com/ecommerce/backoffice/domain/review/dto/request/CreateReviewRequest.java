package com.ecommerce.backoffice.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
        @NotBlank(message = "평점은 필수 입력 항목입니다.")
        @Size(min = 1, max = 5, message = "1에서 5사이의 값만 입력해주세요.")
        int rating,

        @NotBlank(message = "평점은 필수 입력 항목입니다.")
        @Size(min = 10, message = "리뷰는 10자 이상 입력해주세요.")
        String description
) {
}
