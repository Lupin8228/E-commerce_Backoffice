package com.ecommerce.backoffice.domain.order.service;

import com.ecommerce.backoffice.domain.order.dto.request.CancelOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.request.UpdateStatusOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.response.CancelOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.UpdateStatusOrderResponse;
import com.ecommerce.backoffice.domain.order.entity.Order;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import com.ecommerce.backoffice.domain.order.repository.OrderRepository;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.enums.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderRepository orderRepository;

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
                () -> new IllegalStateException("없는 주문입니다.")
        );
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
                () -> new IllegalStateException("없는 주문입니다.")
        );

        // 준비중 상태에서만 취소 가능
        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new IllegalStateException("준비중 상태에서만 취소 가능합니다.");
        }

        // 상품 조회
        Product product = order.getProduct();

        // 재고 복구
        int restoreQty = order.getQuantity();
        product.increaseStock(restoreQty);

        // 상품 상태 자동 전환
        if (product.getStatus() != ProductStatus.DISCONTINUED) {
            product.updateStatusByStock();
        }

        order.updateStatusAndCancelReason(OrderStatus.CANCELLED, request.cancelReason());
        return CancelOrderResponse.from(order.getOrderNumber());
    }
}
