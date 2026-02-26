## 🚨 Session_Security_Develop - 관리자 인증/인가 방식 진화(3단계)

관리자 백오피스 API를 만들면서 인증/인가 처리가 아래 순서로 발전했습니다.  
핵심은 **계층 분리 → 결합도 관리 → 선언적 보안** 입니다.

---

### 🔎 배경
관리자 API 대부분은 공통으로 아래 조건이 붙었습니다.

- 로그인 상태여야 접근 가능
- 일부 기능은 **SUPER_ADMIN만 접근 가능**

이 조건을 어디에서/어떤 방식으로 검사하느냐에 따라 코드의 **중복**, **결합도**, **정책 변경 비용**이 달라졌습니다.

---

### 🖼️ 흐름 한눈에 보기

(1단계) Controller + SessionAdminContext  
Request -> Controller -> SessionAdminContext(세션/권한 확인) -> Service -> DB

(2단계) Service + HttpSession  
Request -> Controller -> Service(HttpSession 직접 접근) -> DB

(3단계) Spring Security  
Request -> Security Filter Chain -> SecurityContext(Authentication) -> Controller(Principal 주입) -> Service -> DB

---

### 🔑 1단계) Controller + SessionAdminContext
계층 분리 개선

초기에는 세션 접근/검증을 `AdminSessionContext(SessionAdminContext)`로 모으고, Controller에서 한 줄로 검증하도록 구성했습니다.  
Controller는 인증/인가만 위임하고, Service는 비즈니스 로직에 집중하는 형태였습니다.

```java
// 목적: Controller에서 세션 기반 SUPER_ADMIN 권한을 선검증 후 비즈니스 로직 호출
@PatchMapping("/{adminId}")
public ResponseEntity<AdminUpdateResponse> update(
    @PathVariable Long adminId,
    @RequestBody AdminUpdateRequest requestBody,
    HttpServletRequest request
) {
    adminSessionContext.requireSuperAdmin(request);

    AdminUpdateResponse response = adminManagementService.updateAdminInfo(adminId, requestBody);
    return ResponseEntity.ok(response);
}
```

정리
- 인증/인가 정책이 `AdminSessionContext` 한 곳에 모임
- Controller는 “검증 호출 + 요청 처리”로 단순해짐
- Service는 세션을 모르고 비즈니스 로직에 집중 가능
- 정책 변경 시 수정 지점이 `AdminSessionContext`로 좁혀짐

---

### 🛡️ 2단계) Service + HttpSession
빠르지만 결합도 높음

리팩토링/구현 과정에서 Service에서도 `HttpSession`을 직접 받아 처리한 구간이 있었습니다.  
세션 값을 서비스에서 매번 꺼내 비교하는 대신, **세션 검증 로직을 `SessionAdminContext`로 위임**해서 서비스 코드를 짧게 유지했습니다.

```java
// 목적: Service에서 HttpSession을 받고 SessionAdminContext로 권한 검증 후 처리(결합도 존재)
@Transactional
public UpdateAdminResponse updateInfo(HttpSession session, UpdateAdminRequest request, Long adminId) {

    sessionAdminContext.requireSuperAdmin(session);

    Admin admin = findById(adminId);
    admin.updateInfo(request);

    return UpdateAdminResponse.from(admin);
}
```

#### SessionAdminContext 클래스

```java
// 목적: 세션 기반 로그인/권한 체크를 공통화한 컨텍스트
@Component
public class SessionAdminContext {

    public void requireLogin(HttpSession session) {
        if (session == null || session.getAttribute("ADMIN_ID") == null) {
            throw new CommonException(CommonError.ADMIN_NOT_LOGGED_IN);
        }
    }

    public void requireSuperAdmin(HttpSession session) {
        requireLogin(session);

        AdminRole role = (AdminRole) session.getAttribute("ADMIN_ROLE");
        if (role != AdminRole.SUPER_ADMIN) {
            throw new CommonException(CommonError.FORBIDDEN_SUPER_ADMIN_ONLY);
        }
    }
}
```

정리
- Service가 `HttpSession`(웹/서블릿)에 의존 → 결합도 상승
- 세션/권한 검증은 `SessionAdminContext`로 공통화 → 서비스 코드는 짧게 유지 가능
- 다만 인증/인가가 Service 계층에 들어가므로 계층 분리 관점에서는 한계가 있음
- 테스트/유지보수 시 세션 mocking 등 부하 증가 가능

---

### 🍃 3단계) Spring Security
선언적 보안, 가장 권장

최종적으로는 Spring Security의 **메서드 보안**을 사용해 접근 제어를 선언적으로 처리했습니다.  
컨트롤러 상단에서 권한 조건이 보이기 때문에 “이 API는 누가 접근 가능한지”가 바로 드러납니다.

```java
// 목적: Spring Security로 SUPER_ADMIN 접근을 선언적으로 제한(메서드 보안)
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
@GetMapping("/admins/{adminId}")
public ResponseEntity<ApiResponse<GetAdminDetailResponse>> getOne(
    @PathVariable Long adminId
) {
    GetAdminDetailResponse response = adminService.getOne(adminId);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
}
```

포인트
- `@PreAuthorize("hasAuthority('SUPER_ADMIN')")`  
  → Spring Security 메서드 보안(권한 기반 접근 제어)

추가 정리
- 접근 제어가 “컨트롤러 상단”에 선언되어 있어 API 의도가 바로 보임
- Controller/Service에서 세션을 직접 다루는 코드가 줄어듦
- 권한 정책이 바뀌어도 어노테이션/권한 매핑 중심으로 정리 가능

---

### 🧩 마지막 정리
세 방식 모두 “동작”은 하지만, 유지보수 관점에서 차이가 컸습니다.

- 세션 기반(1~2단계)은 요구사항이 명확할 때 빠르게 적용 가능
- 다만 Service에서 세션을 직접 만지는 순간 결합도가 올라가 정리 비용이 커짐
- Spring Security(3단계)는 권한 체크를 선언적으로 만들 수 있어 규모가 커질수록 유리

---
