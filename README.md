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

### 4. Form submit 검증
데이터 유효성 검사는 굉장히 중요합니다. 데이터베이스에 쓰레기 값이 저장되면 안되기 때문에 항상 주의해야 합니다.

* JSR 303 Bean Validation (@Valid)
  * 값의 길이, 필수값 (필드검증)
* 커스텀 검증([Validator](https://docs.spring.io/spring-framework/reference/core/validation/validator.html))
  * 중복 이메일, 닉네임 여부 확인
* 폼 에러 있을 시, 폼 다시 보여주기

부족한점
* 메서드 이름 : signUp -> signUpSubmit
* @ModelAttribute : 복합 객체로 받으려고 할때 > 파라미터로 사용할때 생략할 수 있습니다
* @Pattern() : data binding validation시, 정규식으로 필터링
  * 정규표현식 : ^~~$ (시작과 끝을 적어두고 출발하면 편합니다)
  * @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$") // 자주 쓰이는 패턴이니 사용하시면서 익숙해지실 겁니다

잘한점
* 파라미터 validation 애노테이션 활용 잘했음

### 4-1. Work Flow
* 데이터 유효성 검사
  * `@Valid` : 클라이언트가 입력한 필드값에 대한 단순 검증 (형식적인 검증)
  * `Validator` : 비지니스 로직에 따른 복잡한 검증 (DB를 참조하여 조작이 불가능한 실제 데이터와 비교, 신뢰성이 높은 검증)
    * email / nickname duplicate check from DB (find보다는 exists로 구현하는게 목적에 부합하다고 생각함)
* Errors O -> form 화면
* Errors X -> DB 중복체크 -> DB 저장 (단방향 : service -> repository)
* 최종 반환 : 리다이렉트 루트 (redirect:/)

### 몰랐던 부분
* `@Repository`: 데이터 접근 계층에서 예외 변환 기능을 활성화하고, 데이터베이스 관련 로직을 포함하는 클래스를 Spring Bean으로 등록.
* `@Controller` : 프레젠테이션 계층에서 HTTP 요청을 처리하고, 뷰와 데이터를 반환하는 컨트롤러 클래스로 Spring MVC와 연동.
* `@Service` : 비즈니스 로직을 처리하는 서비스 계층의 클래스를 명시하며, 이를 Spring Bean으로 등록하여 가독성을 높임.
* Repository에 @Transactional(readOnly = true)을 붙이는 이유
  * 성능 최적화 : 단순 조회의 경우, 변경 감지 로직 처리를 하지 않음
  * 명확한 의도 전달 : 해당 트랜잭션이 단순 조회임을 분명히 함
* `@Transactional` 알고 쓰자.
  * 정의 : 데이터베이스 작업을 하나의 단위로 묶어서 처리할 때, 하나의 트랜잭션 범위 내에서 수행될 수 있도록 합니다.
  * 레포지토리에 적용할때 : 단순 조회인 경우, 성능 최적화를 명확하게 전달
  * 서비스에 적용할 때 : 비지니스 로직을 수행하면서 데이터 수정/삭제/추가 등 JPA 매커니즘(변경감지로직)에 의해 수행
* `@InitBinder` : 바인딩 되는 객체에 대해서 선언될때마다 validator 작업을 자동으로 해줍니다.
* `@Component` : 스프링 애플리케이션 컨텍스트가 관리하는 빈으로 등록 및 동작할 수 있도록 해줍니다.

### 5. Form submit 처리

* 회원 정보 저장
* 인증 이메일 발송 
* 처리 후 첫 페이지로 리다이렉트 (Post-Redirect-Get)

### 5-1. Work Flow
* 회원 정보 저장
* 인증 이메일 발송
  * 임시 토큰 발급 - UUID
  * 이메일 발송 (JavaMailSender)

### 몰랐던 부분
* password encoding 필요 -> token. 평문 저장시 굉장히 위험한 서비스. 고객 정보 유출됨. 아주 큰일남.
* spring mail sender 가 어떻게 동작하는지 -> 이거 찾아서 공부할 것
  * 메세지 생성 : SimpleMailMessage 객체를 생성해서 수신자/제목/내용을 만들어줍니다
  * 메일 송부 : JavaMailSender.send()
* Profile 설정 : @Profile(name) 어노테이션 추가 후, yml 파일에 `profiles.active=local`
* Token 발급 : UUID를 이용한 임시 인증 토큰 생성
  * UUID(Universally Unique Identifier)로 36자(32자 + 4개의 하이픈) 길이를 가집니다.

### 내 생각과는 다른 부분
* controller 바로 repository 저장해도 된다. 서비스에서 사실상 전달만 하기 때문에. 아무런 기능을 못함

### 알아두면 편리한점
* 기능 구현을 하고, 테스트하기 전에 해당 기능이 잘 동작하는지 항상 로그를 찍어볼 것


