<h1 align="center">🛒 E-Commerce Backoffice</h1>

<p align="center">
  관리자용 이커머스 백엔드 서비스<br>
</p>

---

<!-- ===================== -->
<!-- 🏷 기술 뱃지 -->
<!-- ===================== -->

<p align="center">

<img src="https://img.shields.io/badge/Java-17-red">
<img src="https://img.shields.io/badge/SpringBoot-4.x-green">
<img src="https://img.shields.io/badge/JPA-Hibernate-orange">
<img src="https://img.shields.io/badge/MySQL-8-blue">
<img src="https://img.shields.io/badge/Gradle-8-02303A">
<img src="https://img.shields.io/badge/GitHub-Repository-black">

</p>

---

## 📌 프로젝트 소개

이 프로젝트는 **이커머스 관리자 시스템(Backoffice)** 으로  
주문, 상품, 관리자, 고객, 리뷰 데이터를 관리하고  
**대시보드를 통해 비즈니스 지표를 빠르게 확인**할 수 있도록 만든 백엔드 서비스입니다.

단순 CRUD 구현이 아니라

- ✔ 대시보드 집계 쿼리 단일화
- ✔ DTO Projection 기반 조회 구조
- ✔ Soft Delete 전략 적용
- ✔ 대량 트래픽을 고려한 Repository 설계

에 초점을 맞춘 **포트폴리오용 실무 구조 프로젝트**입니다.

---

## 👥 팀소개

| 이름  | 역할 | 담당 |
|-----|---|---|
| 최길중 | Backend Developer | 주문 / 상품 / 대시보드 / 데이터 구조 설계 |
| 박영수 | Backend Developer | 주문 / 상품 / 대시보드 / 데이터 구조 설계 |
| 박소영 | Backend Developer | 주문 / 상품 / 대시보드 / 데이터 구조 설계 |
| 김소현 | Backend Developer | 주문 / 상품 / 대시보드 / 데이터 구조 설계 |
| 홍성현 | Backend Developer | 주문 / 상품 / 대시보드 / 데이터 구조 설계 |
| 소수경 | Backend Developer | 주문 / 상품 / 대시보드 / 데이터 구조 설계 |

<br>

### [📎프로젝트 노션 바로가기](https://www.notion.so/teamsparta/2-2ff2dc3ef514805aa074fd80c0ad353d)

<br>

---

## 🚀 주요 기능

### Admin
- 관리자 상태 관리
- Soft Delete 탈퇴 처리

### Customer
- 고객 상태 관리
- Soft Delete 탈퇴 처리

### Dashboard
- 총 매출 / 기간 매출 집계
- 주문 상태별 카운트
- 리뷰 평점 분포 차트
- 고객 상태 분포
- 카테고리 분포

### Order
- 주문 조회 / 상세 조회
- 주문 상태 변경
- 최근 주문 목록 조회
- 주문번호 자동 생성

### Product
- 상품 등록 / 수정 / 삭제
- 카테고리별 조회
- 재고 관리

### Review
- 평점 분포 통계
- 상품 리뷰 조회

---

## ⏲️ 개발기간
- 2026.02.19(목) ~ 2026.02.26(목)

---

## 🧠 적용 기술

### ✔ Backend Core
- Spring Boot
- Spring Data JPA
- DTO Projection Query
- Soft Delete (@SQLDelete)

### ✔ Performance Optimization
- 집계 쿼리 단일화
- DTO 직접 생성 쿼리
- 엔티티 로딩 제거

### ✔ Architecture Strategy
- Controller / Service / Repository 분리
- BaseEntity 공통화
- 응답 DTO 표준화

---

## 🖥 Development Environment

| 항목 | 버전 |
|---|---|
| Java | 17 |
| Spring Boot | 3.x |
| Gradle | 8.x |
| MySQL | 8.x |
| JPA | Hibernate |
| IDE | IntelliJ |

---

## 🧩 Architecture

<p align="center">
  <img src="docs/architecture.png" width="80%">
</p>

---

## 🖼 API 명세서

<p align="center">
  <img src="docs/images/.png" width="80%">
</p>

---

## 🗄 ERD Diagram

<p align="center">
  <img src="docs/images/ERD_diagram.png" width="80%">
</p>

---

## 🔧 Technologies & Tools (BE)

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> 
<img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"/> 
<img src="https://img.shields.io/badge/JSONWebToken-000000?style=for-the-badge&logo=JSONWebTokens&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/>
<img src="https://img.shields.io/badge/CODEDEPLOY-181717?style=for-the-badge"/>
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"/>
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/>  
<img src="https://img.shields.io/badge/GithubActions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"/>
<img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"/>  
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/> 
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/>

---

## 📈 프로젝트 파일 구조

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
```

---

## 🚨 Trouble Shooting


---

