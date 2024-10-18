# 프로젝트로 배우는 스프링
reference : [커리큘럼 스프링과 JPA 기반 웹 애플리케이션 개발, 백기선](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-JPA-%EC%9B%B9%EC%95%B1/dashboard)
* 하게 된 이유 : 현업에서 막막하게 개발해야하는 상황을 미리 체험하고 극복하기 위해 어떤 노력이 더 필요한지 가감없이 느껴보기 위함
* 프로젝트 시작일 : 10/16, 9:40PM 
* 프로젝트 종료일 : tbd

## Feature1 : 회원가입
* Domain 개발
* Controller 개발

### 1. 도메인 개발
Account 도메인에 필요한 데이터는 **DB에 저장할 필요가 있는지 에 대한 고민**부터 시작해야 합니다.
* 로그인
  * 이메일/닉네임/패스워드/검증절차(이메일 검증/토큰)/가입날짜(검증 완료 후)
* 프로필
  * 소개글/직업/url/거주지/프로필사진
* 알림설정
  * notice path(web or email)

### 1-1. Work Flow  
각 기능에서 필요로 하는 필드값을 명확하게 정의해야 합니다. 1)로그인 과정에서 email-checked 한다던가 2)프로필을 통해 보여주고자 하는 데이터 등이 포함되겠습니다.

* 로그인할때 필요한 ID(unique)/PW
* 검증된 회원인지 체크/토큰
* 검증됐다면 가입날짜
* 프로필 생성
* 커뮤니티 관련 알림설정

### 2. 컨트롤러 개발
GET("/sign-up") 요청시 view 페이지를 보여주는 컨트롤러를 개발합니다. 개발 후 테스트까지 실행합니다.

### 2-1. Work Flow
페이지를 보여주는데서 끝나는게 아니라, 추가 설정과 테스트를 반드시 수행합니다.

* spring MVC
* security access 를 위한 config 설정
* 테스트

### 3. 회원가입 뷰
어느정도 작업한 히스토리가 있으면 그걸 복붙해서 추가 디벨롭해도 될 부분이라고 생각합니다.

### 3-1. Work Flow
* view 기본 페이지 설정
* css style 적용
* 테스트 (model.attribute)

### 3-2. 몰랐던 부분
//TODO: Main README에 별도로 리스트업할 것
* 부트스트랩 설정
* dev-tools : 서버를 껐다가 켜지 않아도 build를 통해서 뷰 페이지 변경사항을 적용해줍니다.
* Image 추가 : images 패키지에 저장하고, 정적 리소스에 대한 security 설정 추가 필요합니다.
* model.addAttribute 관련 : 객체만 생성해서 넘겨도 spring MVC에서 자동으로 camelCase로 변환해서 뷰로 전달합니다.
* root page 설정 : resources/templates-index.html 을 기본적으로 루트 페이지로 설정함 (이걸 몰랐냐..심각하다)

### 4. Form submit 검증
진짜 처참하네. 애노테이션만 달줄 알고, 여기서 추가 기능 확장이 안됨. 어떻게 처리해아하는지. 심지어 @Valid 만 검증되면 별도의 테스트 코드는 안짜도 되는지도 모르겠네.. 현재 수준이 심각하다.. 밥이 넘어가고 운동을 하고 싶냐?

회원 가입 폼 검증
* JSR 303 애노테이션 검증 -> `바인딩되는 데이터에 대한 @Valid 처리` 를 할 수 있는가
값의 길이, 필수값

* 커스텀 검증
중복 이메일, 닉네임 여부 확인 > 이건 아예 몰랐음.... implements Validator

* 폼 에러 있을 시, 폼 다시 보여주기.

부족한점
* 메서드 이름 : signUp -> signUpSubmit
* @ModelAttribute : 복합 객체로 받으려고 할때 > 파라미터로 사용할때 생략할 수 있습니다
* @Pattern() : data binding validation시, 정규식으로 필터링
  * 정규표현식 : ^~~$ (시작과 끝을 적어두고 출발하면 편합니다)
  * @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$") // 자주 쓰이는 패턴이니 사용하시면서 익숙해지실 겁니다

잘한점
* 파라미터 validation 애노테이션 활용 잘했음

내가 구현한 코드
* 사실 기억에 남아서 떠올리면서 작업을 한 것이지, 고민의 흔적이 없어
```java
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        FieldError fieldError = fieldErrors.get(0);
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();
        log.info("field = {}, defaultMessage = {} ", field, defaultMessage);
```

### 3-1. Work Flow
* 데이터 유효성 검사 @Valid
* Errors O -> form 화면
* Errors X -> DB 중복체크 -> DB 저장 (단방향 : service -> repository)
* 최종 반환 : 리다이렉트 루트 (redirect:/)