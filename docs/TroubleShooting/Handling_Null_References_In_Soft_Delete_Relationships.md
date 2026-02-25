# Handling Null References in Soft Delete Relationships - Soft Delete 연관관계에서 발생한 Null 참조 처리

---

## 🔎 배경
프로젝트 요구 사항에 따라 주문(Order) 조회 시 관리자(Admin), 고객(Customer), 상품(Product) 정보를 함께 반환해야 했습니다.  
또한 해당 프로젝트는 Soft Delete 방식을 사용하고 있으며, 이로 인해 연관 엔티티가 삭제될 경우 조회 과정에서 예상치 못한 문제가 발생했습니다.

---

## ⚠️ 발단

**기존 코드**
```java
   public static GetOrdersResponse of(Order order) {
        String adminEmail = null;
        if(order.getAdmin() != null) {
            adminEmail = order.getAdmin().getEmail();
        }

        String customerName = null;
        if(order.getCustomer() != null) {
            customerName = order.getCustomer().getName();
        }

        String productName = null;
        if(order.getProduct() != null) {
            productName = order.getProduct().getName();
        }

        return GetOrdersResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customer(customerName)
                .product(productName)
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus().name())
                .admin(adminEmail)
                .build();
    }
```
위와 같이 코드를 작성한 경우, 관리자, 고객, 상품 Entity가 삭제되었을 때 연관 객체가 null이 되어 NullPointerException이 발생하였습니다.  
이로 인해 주문 목록 조회 및 상세 조회 API가 정상적으로 동작하지 않고 전체 요청이 실패하는 문제가 발생합니다.

---

## 🔧 전개
Soft Delete를 사용할 경우 연관 객체가 항상 존재한다고 가정할 수 없다는 점을 인지하였고, 이에 따라 연관 객체 접근 시 null 가능성을 고려하도록 코드를 수정하였습니다.

**개선 코드**
```java
public static GetOrdersResponse of(Order order) {

    String adminEmail = null;
    if (order.getAdmin() != null) {
        adminEmail = order.getAdmin().getEmail();
    }

    String customerName = null;
    if (order.getCustomer() != null) {
        customerName = order.getCustomer().getName();
    }

    String productName = null;
    if (order.getProduct() != null) {
        productName = order.getProduct().getName();
    }

    return GetOrdersResponse.builder()
            .id(order.getId())
            .orderNumber(order.getOrderNumber())
            .customer(customerName)
            .product(productName)
            .quantity(order.getQuantity())
            .totalPrice(order.getTotalPrice())
            .createdAt(order.getCreatedAt())
            .status(order.getStatus().name())
            .admin(adminEmail)
            .build();
}
```

기존 구현에서는 주문 조회 시 Order에서 관리자, 고객, 상품과 같은 연관 객체가 항상 존재한다고 가정한 채 바로 필드에 접근하여 값을 가져왔기 때문에 Soft Delete로 인해 해당 엔티티가 null인  
경우 NullPointerException이 발생했으나 이후 문제점을 파악 후, 각 연관 객체에 접근하기 전에 null 여부를 선검증 하여 값이 존재할 때만 이메일, 이름 등의 정보를 추출하고 없을 경우 기본값 null을 유지하도록 처리함으로써  
예외 발생을 방지하고 삭제된 데이터가 포함되더라도 주문 목록 조회 및 상세조회 API가 안정적으로 동작하도록 하였습니다.

---

## 💡 이번 트러블 슈팅을 통해 배운점
Soft Delete 방식을 사용할 경우 데이터가 실제로 삭제되지 않고 논리적으로만 제거되기 때문에, 연관 관계에 있는 엔티티가 항상 존재할 것이라고 단정해서는 안 된다는 점을 배웠으며,  
특히 주문 조회 과정에서 Order 와 연결된 관리자·고객·상품 정보가 null이 될 수 있다는 가능성을 고려하지 않으면 단순 조회 로직에서도 예외가 발생하여 전체 API 안정성이 크게 저하된다는 것을 경험했습니다.


