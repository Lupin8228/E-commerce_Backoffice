package com.ecommerce.backoffice.domain.order.service;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.domain.order.dto.request.CancelOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.request.UpdateStatusOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.response.CancelOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.UpdateStatusOrderResponse;
import com.ecommerce.backoffice.domain.order.entity.Order;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import com.ecommerce.backoffice.domain.order.repository.OrderRepository;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderRepository orderRepository;
    private final AdminRepository adminRepository;

    /**
     * 주문 상태 변경
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public UpdateStatusOrderResponse updateStatusOrder(Long id, UpdateStatusOrderRequest request) {
        // 주문 데이터 있는지 확인
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new CommonException(CommonError.ORDER_NOT_FOUND)
        );

        // 이미 배송완료/취소된 주문은 변경 금지
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new CommonException(CommonError.ORDER_ALREADY_DELIVERED);
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new CommonException(CommonError.ORDER_ALREADY_CANCELLED);
        }

        // 주문 상태 update
        order.updateStatus(request.status());
        return UpdateStatusOrderResponse.of(order.getOrderNumber(), request.status());
    }

    /**
     * 주문 취소
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public CancelOrderResponse cancelOrder(Long id, CancelOrderRequest request) {
        // 주문 데이터 있는지 확인
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new CommonException(CommonError.ORDER_NOT_FOUND)
        );

        // 준비중 상태에서만 취소 가능
        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new CommonException(CommonError.ORDER_NOT_PREPARING);
        }

        // 상품 조회
        Product product = order.getProduct();

        // 상품이 삭제되지 않았을 때만 재고 복구
        if (!product.isDeleted()) {
            // 재고 복구
            int restoreQty = order.getQuantity();
            product.increaseStock(restoreQty);

            // 상품 상태 자동 전환
            if (product.getStatus() != ProductStatus.DISCONTINUED) {
                product.updateStatusByStock();
            }
        }

        order.updateStatusAndCancelReason(OrderStatus.CANCELLED, request.cancelReason());
        return CancelOrderResponse.from(order.getOrderNumber());
    }
}
