## 📂 Project Structure (Domain-Driven Design)
본 프로젝트는 유지보수와 협업의 효율성을 위해 **도메인 기반 패키지 구조**를 채택하고 있습니다.

```text
src/main/java/com/commerce/manageit/
├── domain/                    # 핵심 비즈니스 로직 (도메인별 분리)
│   ├── customer/              # 고객(Customer) 관련 도메인
│   │   ├── controller/        # API 엔드포인트
│   │   ├── service/           # 비즈니스 로직
│   │   ├── repository/        # DB 접근 계층
│   │   ├── entity/            # JPA 엔티티 (Domain Model)
│   │   └── dto/               # Request / Response 객체
│   └── product/               # 상품(Product) 관련 도메인
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       └── dto/
├── global/                    # 프로젝트 전역 공통 설정
│   ├── config/                # Framework 설정 (Security, Swagger, JPA 등)
│   ├── error/                 # 예외 처리 (ExceptionHandler, ErrorCode)
│   ├── common/                # 공통 추상 클래스 (BaseEntity, ApiResponse)
│   └── util/                  # 유틸리티 및 확장 함수 (Extension, Utils)
└── ManageItApplication.java   # 프로젝트 메인 실행 클래스