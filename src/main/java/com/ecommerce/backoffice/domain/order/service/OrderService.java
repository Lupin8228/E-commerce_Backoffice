package com.ecommerce.backoffice.domain.order.service;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.repository.CustomerRepository;
import com.ecommerce.backoffice.domain.order.dto.request.CreateOrderRequest;
import com.ecommerce.backoffice.domain.order.dto.request.OrderSearchRequest;
import com.ecommerce.backoffice.domain.order.dto.response.CreateOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.GetOrderResponse;
import com.ecommerce.backoffice.domain.order.dto.response.GetOrdersResponse;
import com.ecommerce.backoffice.domain.order.entity.Order;
import com.ecommerce.backoffice.domain.order.enums.OrderStatus;
import com.ecommerce.backoffice.domain.order.repository.OrderRepository;
import com.ecommerce.backoffice.domain.order.utils.OrderNumberGenerator;
import com.ecommerce.backoffice.domain.product.entity.Product;
import com.ecommerce.backoffice.domain.product.repository.ProductRepository;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.backoffice.domain.admin.entity.QAdmin.admin;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final OrderNumberGenerator orderNumberGenerator;


    private Admin getLoginAdmin() {
        return (Admin) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // 주문 생성
    @Transactional
    public CreateOrderResponse save(Long adminId, CreateOrderRequest request) {

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new CommonException(CommonError.ADMIN_NOT_FOUND));

        // 사용자 조회
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new CommonException(CommonError.USER_NOT_FOUND));

        // 상품 조회
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new CommonException(CommonError.PRODUCT_NOT_FOUND));

        // 상품 재고 확인
        if (product.getStock() < request.quantity()) {
            throw new CommonException(CommonError.PRODUCT_OUT_OF_STOCK);
        }

        // 재고 차감
        product.decreaseStock(request.quantity());

        Order order = Order.builder()
                .orderNumber(orderNumberGenerator.generate())
                .status(OrderStatus.PREPARING)
                .totalPrice(product.getPrice() * request.quantity())
                .quantity(request.quantity())
                .customer(customer)
                .product(product)
                .admin(admin)
                .build();

        Order saved = orderRepository.save(order);

        return CreateOrderResponse.of(
                saved.getOrderNumber(),
                saved.getCustomer(),
                saved.getProduct()
        );
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<GetOrdersResponse> findOrders(
            OrderSearchRequest request,
            int page,
            int size
    ) {
        Sort.Direction dir = request.direction().equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(dir, request.sortBy()));

        return orderRepository.searchOrders(request, pageable)
                .map(GetOrdersResponse::of);
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public GetOrderResponse getOne(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CommonException(CommonError.ORDER_NOT_FOUND)
        );
        return new GetOrderResponse(
                order.getOrderNumber(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getProduct().getName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus().name(),
                order.getAdmin().getName(),
                order.getAdmin().getEmail(),
                order.getAdmin().getRole().name()
        );
    }
}
