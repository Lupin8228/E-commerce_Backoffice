# 🔢 rating 데이터 자료형 (int -> Integer)변경

---

## 🔎 배경
리뷰 도메일 개발중 평점 필드를 구현하면서 초기에는 기본 자료형인 int를 사용하였다. 
하지만 리뷰 리스트 조회 API에서 rating값을 선택하지 않고 조회했을때 에러가 뜨는 현상이 발생했다.

---

## ⚔️ 전개
### 문제 1
```java
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<GetReviewPageResponse>> getAllReviews(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer rating, // int에서 Integer로 변경
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sort
    ){
        GetReviewPageResponse response = reviewService.getAllReview(search, rating, page, size, sortBy, sort);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
```
사용자가 리뷰 목록을 조회할 때 평점 필터링을 하지않고 조회하고 싶을때 rating 파라미터에 값이 들어오지 않게 되는데, 
만약 자료형이 int라면 스프링은 null을 담을 수 없어 에러를 내거나 기본값인 0을 강제로 넣을것이다.
그러면 Request Dto의 rating의 값이 @NotNull과 @Min 조건으로 인해 에러를 일으키게 된다.
그래서 int 였던것을 Integer로 변경해 사용함으로써 평점 필터링 조건 없음 상태를 null로 받을 수 있게 수정했다.

### 문제 2
```java
    public record CreateReviewRequest(
        @NotNull(message = "평점은 필수 입력 항목입니다.")
        @Min(value = 1, message = "1에서 5사이의 값만 입력해주세요.")
        @Max(value = 5, message = "1에서 5사이의 값만 입력해주세요.")
        Integer rating,

        @NotBlank(message = "리뷰는 필수 입력 항목입니다.")
        @Size(min = 10, message = "리뷰는 10자 이상 입력해주세요.")
        String description
    ) {
    }
```
int 자료형은 null이 될 수 없고 값이 들어오지 않으면 기본값이 0이 들어가게 된다.
그래서 int를 사용했을때 NotNull 검증은 필요 없는것이었고, 만약 사용자가 값을 입력하지 않았음에도 Min 검증으로 인해 1에서 5사이의 값만 입력해달라는 경고 메시지가 뜨게된다.
이는 사용자 입장에서 0을 넣은건지 값을 입력하지 않은건지 알 수 없기 때문에 부적절한다.
하지만 Integer를 사용하면, 값이 반드시 들어와야 한다는 검증과 1에서 5사이의 값만 들어와야 한다는 검증 로직이 더 정확해질수 있다.

---

## ✨ 해결 방안
컨트롤러의 @RequestParam 및 DTO의 필드 타입을 Integer로 변경하였습니다.
그리고 rating이 사용되고 있는 나머지 파일들도 int에서 Integer로 변경해주었습니다.
결과적으로 Integer 래퍼 클래스를 도입 해줌으로써 사용자가 평점 필터를 선택하지 않았을 때의 상태를 null로 받을수 있게 되었습니다.
