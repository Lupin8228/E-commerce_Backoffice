package com.ecommerce.backoffice.domain.order.service;

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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final OrderNumberGenerator orderNumberGenerator;


    // 주문 생성
    @Transactional
    public CreateOrderResponse save(CreateOrderRequest request, HttpServletRequest sessionRequest) {
        // Admin admin = getLoginAdmin(session); <- 아직 구현안됨

        // 관리자 조회
//        Admin admin = (Long) session.getAttribute("LOGIN_ADMIN");
//        if(admin == null) {
//            throw new RuntimeException("관리자 계정에 로그인하시기 바랍니다.");
//        }

        // 사용자 조회
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 상품 조회
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 상품 재고 확인
        if (product.getStock() < request.quantity()) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        // product.decreeaseStock(request.quantity()); <- 아직 구현안됨

        Order order = Order.builder()
                .orderNumber(orderNumberGenerator.generate())
                .status(OrderStatus.PREPARING)
                .totalPrice(product.getPrice() * request.quantity())
                .quantity(request.quantity())
                .customer(customer)
                .product(product)
                // .admin(admin) <- 아직 구현안됨
                .build();

        return CreateOrderResponse.of(order.getOrderNumber(), order.getCustomer(), order.getProduct());
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
        // Admin admin = getLoginAdmin(session); <- 아직 구현안됨

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
                () -> new RuntimeException("존재하지 않는 주문입니다.")
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
