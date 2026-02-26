package com.ecommerce.backoffice.domain.admin.dto.response;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import org.springframework.data.domain.Page;

import java.util.List;

public record GetAdminListResponse(
        List<GetAdminListItemResponse> admins,
        GetPageInfoResponse pageInfo
) {
    public static GetAdminListResponse from(Page<Admin> page) {
        return new GetAdminListResponse(
                page.getContent().stream().map(GetAdminListItemResponse::from).toList(),
                GetPageInfoResponse.from(page)
        );
    }
}
