## 🚨 Customer Search API - QueryDSL & Paging 설계 학습 정리

### 🔎 배경
관리자 백오피스에서 고객 조회 API를 구현하면서 다음 요구사항이 있었습니다.

- 고객 기본 정보 조회
- 고객별 주문 수 (COUNT)
- 고객별 총 주문 금액 (SUM)
- 취소 주문 제외
- 이름/이메일 검색 기능
- 페이징 처리
- 동적 정렬 지원

이를 만족하기 위해 Spring Data JPA 기본 메서드 대신 QueryDSL 기반 커스텀 조회로 구현했습니다. 

특히, 고객별 주문 통계를 함께 조회해야 했기 때문에 LEFT JOIN + GROUP BY + 집계 + 페이징이 결합된 구조였습니다.

QueryDSL과 페이징 구현을 처음 해보는 입장에서 웹 서핑을 통해 기능 구현 자체는 가능했지만, 

단순히 “동작하는 코드”를 작성하는 것과 “왜 이런 구조가 필요한지 이해하는 것”은 전혀 다른 영역이라는 것을 느끼게 되었습니다.

특히 다음 세 가지 개념에 대한 이해가 확장되었습니다.

1. 동적 정렬은 단순 문자열 처리 문제가 아니다.

2. GROUP BY 환경에서의 페이징은 구조적으로 다르다.

3. LEFT JOIN에서 ON과 WHERE은 논리적으로 같지 않다.

---

## 1. 동적 정렬 구현 - 문자열이 아닌 타입 시스템의 문제
### 🔎 문제 상황

Spring Data의 Pageable은 문자열 기반 정렬 정보를 제공합니다.
```
?sort=name,asc
?sort=createdAt,desc
```
하지만 QueryDSL은 문자열 기반 쿼리 빌더가 아니라, 타입 안정성을 보장하는 DSL이기 때문에,
문자열을 직접 orderBy에 전달할 수 없고 컴파일 타임에 타입이 확정된 Path 객체가 필요합니다.
```
customer.name.asc();   
```

즉, 정렬 기준으로 입력된 "name"이라는 문자열을 바로 사용할 수 없었습니다.

### 💡 해결 방식

이를 해결하기 위해 PathBuilder를 사용하여 문자열 필드명 → QueryDSL Path 객체로 변환하는 과정을 거쳤습니다.

- PathBuilder → 어떤 컬럼을 정렬할지 동적으로 찾아주는 도구
- OrderSpecifier → 그 컬럼을 ASC/DESC로 정렬하라고 QueryDSL에 전달하는 객체

문자열 필드명을 QueryDSL Path 객체로 변환하기 위해 PathBuilder를 사용하고, 

정렬 순서를 QueryDSL에 전달하기 위해 OrderSpecifier를 사용하였습니다.

```java
private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable){
    return pageable.getSort().stream()
            .map(order -> {
              PathBuilder<Customer> pathBuilder =
                      new PathBuilder<>(Customer.class, "customer");
  
              return new OrderSpecifier( 
                      order.isAscending() ? Order.ASC : Order.DESC,
                      pathBuilder.get(order.getProperty())
              ); 
            })
            .toArray(OrderSpecifier[]::new);
}
```

이를 통해 문자열 "name"→ customer.name Path 객체를 생성하고 OrderSpecifier와 결합하였고

이를 QueryDSL의 orderBy에 사용하여 동적 정렬 기능을 구현 하였습니다.
```
customer.name.asc(); 
```

### 📚 학습한 점

- Spring Data는 추상화된 Repository 레이어
- QueryDSL은 타입 기반 DSL 레이어

두 레이어는 직접 연결되지 않으며, 변환 계층이 필요하고
동적 정렬은 단순 문자열 처리 문제가 아니라 타입 안정성을 유지하며 런타임 동작을 구현하는 문제라는 점을 배우게 되었습니다.

Q타입은 정적으로 생성된 필드만 접근할 수 있지만,
PathBuilder를 사용하면 문자열을 기반으로 Path를 만들기 때문에, 
동적 정렬 과정에서 타입 안정성이 약화된다는 점과 동적 정렬의 본질적 한계를 깨닫게 되었습니다.

실무에서는 런타임 에러 방지를 위한 예외 처리를 필수로 해주어야 한다는 점도 알게되었습니다.

---

## 2. GROUP BY 환경에서의 Page 처리 - Count 쿼리 분리 필요성
### 🔎 문제 상황

고객별 주문 수와 총 주문 금액을 함께 조회하기 위해 다음과 같은 집계 쿼리를 사용했습니다
- LEFT JOIN
- COUNT
- SUM
- GROUP BY customer.id

GROUP BY가 포함된 쿼리는 여러 row를 반환합니다.
예를 들어 고객이 100명이라면, 집계 쿼리는 100개의 row를 반환합니다.

|customer_id|count(o.id)|
|:----|:----|
|1|3|
|2|1|
|3|0|
|..|..|
|100|1|

---
Spring의 Page 객체는 다음 두 가지 정보를 반드시 필요로 합니다.

1. 현재 페이지 데이터

2. 전체 데이터 개수 (totalElements)

집계 쿼리의 목적과 Page의 total 계산 목적은 다릅니다.

- 데이터 조회 쿼리의 목적 → 고객별 집계 결과

-  쿼리의 목적 → 전체 고객 수 계산

단순히 count()를 사용하면 고객별 count 결과가 반환되어 
전체 고객 수(단일 숫자)가 아니라 고객별 주문 개수 목록이 반환됩니다.

---

### 💡 해결 방식

집계 쿼리와 cout 쿼리를 명확히 분리했습니다.
```java
Long total = queryFactory
    .select(customer.count())
    .from(customer)
    .where(
        searchCondition(search),
        customer.deleted.eq(false)
    )
    .fetchOne();
```
JOIN과 GROUP BY를 제거하고 오직 고객의 row만 count 할 수 있도록 하여 정확한 totalElements 값을 계산할 수 있도록 하였습니다.

---

### 📚 학습한 점
- GROUP BY가 포함된 집계 쿼리는 count 쿼리로 재사용할 수 없다.
- JOIN이 포함된 count는 row 수를 증가시켜 잘못된 total을 반환할 수 있다.
- 집계 쿼리와 count 쿼리는 목적이 다르므로 반드시 분리해야 한다.

---
## 3. LEFT JOIN에서 ON vs WHERE의 차이
### 🔎 문제 상황

일반적으로 테이블에서 특정 조건에 부합하는 데이터를 조회하고 싶을 때 WHERE을 사용해 왔습니다.

LEFT JOIN으로 customer, order 테이블을 조인할 때, 취소된 주문은 포함시키지 않기 위해 
```
leftJoin(order)
.where(searchCondition(search),order.status.ne(OrderStatus.CANCELLED)))
```
하지만 이 경우 주문이 없는 고객이 조회되지 않는 문제가 발생했습니다.

---

### ⚠️ 원인 분석
```SQL
SELECT ...
FROM customer c
LEFT JOIN orders o
    ON o.customer_id = c.id
WHERE o.status <> 'CANCELLED'
```
SQL 실행 순서:

1. JOIN 수행

2. WHERE 필터 적용

LEFT JOIN은 연관 데이터가 없어도 row를 유지하지만, WHERE에서 조인 대상 컬럼에 조건을 걸면 NULL row가 제거됩니다.

WHERE 조건을 걸기 전 LEFT JOIN 결과가 테이블이 아래와 같다면

|cusomer_id|order_id|status|
|----|----|----|
|1|10|CANCELLED|
|2|NULL|NULL|

WHERE 조건을 적용하면 
- 1번 고객은 주문을 취소해서 탈락,
- 2번 고객은 status = NULL 이라서 탈락

결과적으로 WHERE에 조건을 두면, 주문이 없는 고객도 제거되어 LEFT JOIN이 INNER JOIN처럼 동작하게 됩니다.

---

### 💡 해결 방식

따라서 취소된 주문 제외 조건을 ON 절로 이동했습니다.
```
.leftJoin(order).on(
        order.customer.eq(customer)
        .and(order.status.ne(OrderStatus.CANCELLED))
)
```
```sql
SELECT ...
FROM customer c
LEFT JOIN orders o
    ON o.customer_id = c.id
    AND o.status <> 'CANCELLED'
```
이렇게 하면 CANCELLED 주문은 아예 JOIN 대상에서 제외되고, 주문이 없는 고객은 그대로 NULL을 유지하게 됩니다.

---

### 📚 학습한 점

| 조건 위치 | SQL 형태          | 결과                      |
| ----- | --------------- | ----------------------- |
| WHERE | LEFT JOIN 후 필터링 | 주문 없는 고객 제거 (INNER처럼 됨) |
| ON    | JOIN 대상만 제한     | 고객은 유지, 주문만 필터          |

- JOIN 조건 위치는 단순 문법 차이가 아니라 실행 전략 차이이다.

- ON과 WHERE는 논리적으로 같아 보여도 결과 집합이 달라질 수 있다.

- SQL을 “문법”이 아니라 “데이터 생성 과정”으로 이해해야 한다.

---
### ✨ 설계 관점에서의 성장


이번 구현을 통해 단순히 QueryDSL을 사용한 것이 아니라, 다음과 같은 설계 관점을 학습했습니다.

- 타입 안정성을 유지하는 동적 정렬 설계
- 집계 쿼리와 페이징 계산 쿼리의 분리 전략
- JOIN 실행 단계와 조건 위치에 대한 이해

이 경험을 통해 복잡한 조회 API를 설계할 때 정렬 전략, 집계 전략, 페이징 전략, 

JOIN 조건 설계 등에 대해 이해하고 선택하여 사용할 수 있게 되었습니다.

QueryDSL은 단순한 쿼리 작성 도구가 아니라,
SQL 실행 구조와 집계 동작을 이해하고 설계하는 도구라는 것도 확실히 깨닫게 되었습니다.

