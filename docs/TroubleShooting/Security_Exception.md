## 🚨 API Consistency - Custom AccessDeniedHandler를 통한 예외 응답 규격 통일

### 🔎 배경
Spring Security를 적용한 후, 권한이 없는 리소스에 접근할 때 클라이언트에게 전달되는 응답이 일정하지 않은 문제를 발견했습니다.

초기 상태에서는 인가(Authorization) 실패 시 브라우저나 Postman에 아무런 에러 메시지 없이 403 Forbidden 상태 코드만 노출되었습니다. 
이로 인해 백엔드 내부에서 어떤 인가 오류가 발생했는지 추적이 불가능한 상황이었습니다.

### ⚠️ 발단
우선 어떤 에러인지 확인하기 위해 Security 설정에서 /error 엔드포인트를 허용하여 최소한의 에러 정보를 확인하고자 했습니다.
그 결과 다음과 같은 스프링 기본 에러 응답을 받을 수 있었습니다.
```JSON 
{
    "timestamp": "2026-02-25T12:00:00.000+00:00",
    "status": 403,
    "error": "Forbidden",
    "path": "/api/admins"
}
```

하지만 이 방식에도 여전히 다음과 같은 문제가 존재했습니다.
* 응답 규격 불일치: 프로젝트에서 공통으로 사용하는 ApiResponse<T> 형식이 아닌 스프링 표준 형식이 노출됨.
* 클라이언트 처리 부하: 프론트엔드에서 일반 API 응답과 보안 에러 응답의 구조가 달라 에러 핸들링 로직을 이중으로 작성해야 함.
* 상세 코드 부재: 단순히 Forbidden이 아닌, 비즈니스적으로 정의한 에러 코드를 전달할 수 없음.

### 🔧 전개
근본적인 해결을 위해 인가 예외가 발생하는 시점에서 응답을 직접 가로채어 기존 커스텀 응답 규격을 맞추기로 결정했습니다.
AccessDeniedHandler를 커스텀하여 제가 원하는 ApiResponse 형식으로 응답을 내려주도록 설계를 변경했습니다.

### 🎯 결말
AccessDeniedHandler를 직접 구현하여 시큐리티 필터 체인의 종착지를 컨트롤러와 동일한 규격으로 맞췄습니다.

1️⃣ CustomAccessDeniedHandler 구현
```Java
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // 1. 응답 헤더를 공통 규격(JSON)으로 설정
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 2. 비즈니스 공통 에러 객체 생성
        ApiResponse<Void> apiResponse = ApiResponse.fail(CommonError.PERMISSION_DENIED);
        
        // 3. ObjectMapper를 통해 JSON으로 직접 변환하여 응답 바디에 기입
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
```
2️⃣ SecurityConfig에 핸들러 등록 및 /error 의존 제거
이제 /error 엔드포인트에 의존하지 않고, 시큐리티가 직접 응답을 반환하도록 설정했습니다.

```Java
http.exceptionHandling(exception -> exception
        .accessDeniedHandler(accessDeniedHandler)
);
```

최종적으로 클라이언트는 보안 예외 상황에서도 다른 API와 동일한 구조의 응답을 받을 수 있게 되었습니다.
```JSON
{
  "success": false,
  "data": null,
  "error": {
    "status": 403,
    "code": "A2019",
    "errorMessage": "해당 작업에 대한 접근 권한이 없습니다."
  }
}
```

이 과정을 통해 얻은 성과는 다음과 같습니다.
* 디버깅 효율성: 구체적인 에러 확인 가능.
* API 일관성 확보: 인증/인가 실패 시에도 클라이언트는 단일화된 응답 파싱 로직을 유지할 수 있음.
결과적으로 Spring Security의 예외 처리 메커니즘을 프로젝트의 공통 응답 정책에 맞게 동기화할 수 있었습니다.