## 🚨 Dashboard Query Optimization - 인덱스 설계를 통한 대시보드 성능 개선

### 🔎 배경
관리자 백오피스의 대시보드 기능을 구현하면서  
요약 통계, 위젯 데이터, 차트 데이터, 최근 주문 목록을  
하나의 API에서 동시에 조회하도록 설계했습니다.

이 과정에서 다음과 같은 특징을 발견했습니다.

- COUNT / SUM / GROUP BY 쿼리 다수 발생
- soft delete 조건(`deleted`)이 모든 테이블에 존재
- 상태 필터 + 날짜 조건이 자주 사용됨
- 최근 주문 조회 시 정렬 비용 존재

테스트 데이터에서는 문제가 없었지만,  
데이터 증가 시 **대시보드가 전체 테이블 스캔을 유발할 구조**임을 확인했습니다.

---

### ⚠️ 발단

Hibernate SQL 로그와 EXPLAIN 분석을 통해  
실제 쿼리 실행 방식을 확인했습니다.

```sql
EXPLAIN
select
  count(o1_0.id),
  cast(coalesce(sum(case
                      when o1_0.created_at between '2026-02-23T00:00' and '2026-02-23T23:59:59.999999999'
                        then 1
                      else 0
    end), 0) as signed)
from
  orders o1_0
where
  o1_0.status<>'CANCELLED';
```

| id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | SIMPLE | o1\_0 | null | ALL | null | null | null | null | 1 | 100 | Using where |

그 결과 다음 문제가 확인되었습니다.

- 상태 조건 조회 시 인덱스 미사용
- 날짜 범위 조회 시 풀 스캔 발생
- 매출 SUM 쿼리에서 정렬 및 스캔 비용 증가
- soft delete 컬럼이 인덱스에 포함되지 않아 필터 비용 증가

즉, 현재 구조는  
**데이터가 많아질수록 대시보드 성능이 급격히 저하되는 구조**였습니다.

---

### 🔧 전개
처음에는 다음과 같은 방식으로 해결을 시도했습니다.

- JPA 쿼리 메서드 단순화
- 쿼리 수 줄이기
- DTO 조회 구조 변경

하지만 확인 결과

- 집계 쿼리는 필수라 제거 불가
- COUNT/SUM 자체는 줄일 수 없음
- 애플리케이션 레벨 튜닝 효과 제한적

즉, 문제의 핵심은 **코드가 아니라 DB 인덱스 설계**였습니다.

---

### 🚧 위기
여기서 또 다른 문제가 발생했습니다.

인덱스를 수동으로 DB에 생성하면

- 개발 환경마다 인덱스 누락 가능
- 팀원 DB와 운영 DB 구조 불일치
- 배포 시 인덱스 적용 누락 위험
- 포트폴리오 환경에서 재현 어려움

즉, 단순 인덱스 추가가 아니라  
**운영 환경에서도 안정적으로 유지되는 구조가 필요**했습니다.

---

### 🔥 절정
근본 해결을 위해 다음 방식으로 접근했습니다.

---

#### 1️⃣ 실제 쿼리 패턴 기반 인덱스 설계

단순 컬럼 인덱스가 아닌  
**실제 WHERE / GROUP BY / ORDER BY 패턴 기준**으로 설계했습니다.

예시:

- 고객 상태 조회  
  → `(status, deleted)`

- 주문 통계 조회  
  → `(status, created_at)`  
  → `(status, created_at, total_price)`

- 최근 주문 조회  
  → `(deleted, created_at DESC)`

- 재고 부족 상품 조회  
  → `(stock, deleted)`

- 카테고리 분포 조회  
  → `(category, deleted)`

- 리뷰 평점 분포  
  → `(deleted, rating)`

---

#### 2️⃣ index.sql 파일 도입

애플리케이션 실행 시 자동 적용되도록  
`index.sql` 파일을 작성했습니다.

```sql
CREATE INDEX idx_orders_status_created
    ON orders (status, created_at);

CREATE INDEX idx_orders_status_created_price
    ON orders (status, created_at, total_price);

CREATE INDEX idx_orders_deleted_created_desc
    ON orders (deleted, created_at DESC);
```
또한 기존 인덱스 충돌을 방지하기 위해
DROP INDEX 후 생성하도록 구성했습니다.

---

#### 3️⃣ Spring Boot 실행 시 자동 인덱스 적용 구조 구축

```
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:index.sql
spring.jpa.defer-datasource-initialization=true
spring.sql.init.continue-on-error=true
```

이를 통해:
- JPA가 테이블 생성 완료 후 인덱스 적용
- 환경마다 동일한 DB 구조 유지
- 인덱스 누락 방지
- 서버 실행만으로 DB 최적화 보장

즉, 인덱스를 운영 구조에 포함시켰습니다.

---

###  🎯 결말

인덱스 추가 후 Hibernate SQL 로그와 EXPLAIN 분석을 다시 해본 결과 
type, key 등이 변경된 것을 확인했습니다.

| id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | SIMPLE | o1\_0 | null | index | idx\_orders\_status\_created,idx\_orders\_status\_created\_price | idx\_orders\_status\_created | 10 | null | 1 | 100 | Using where; Using index |

이 구조 적용 후 다음과 같은 개선 효과를 얻었습니다.
- 대시보드 조회 시 풀 스캔 제거
- 상태 + 날짜 조건 쿼리 성능 개선
- 최근 주문 조회 정렬 비용 감소
- 매출 집계 쿼리 속도 개선
- 개발/운영 환경 DB 구조 일관성 확보
- 프로젝트 실행만으로 인덱스 자동 적용

단순 성능 개선을 넘어서
운영 환경을 고려한 DB 초기화 구조까지 설계할 수 있었습니다.

---