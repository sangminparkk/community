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

### 알아두면 편리한점
* 기능 구현을 하고, 테스트하기 전에 해당 기능이 잘 동작하는지 항상 로그를 찍어볼 것

### 5-2. 테스트 코드 작성
로직을 차례대로 따라가면서 unit 테스트로 최대한 빈틈없게 작성할 것. 테스트 성공 뿐만 아니라 실패가 발생하는 상황(**로직 주석처리**)도 연출해보기.
* POST "/sign-up", 파라미터로 데이터 전송 (**html에서 어떤 데이터가 넘어오는가**)
  * spring security + thymeleaf > html hidden csrf token default 적용됨
  * 인증되지 않은 사용자 접근을 받아들이되, 안전하지 않은 요청까지 받아들이진 않는게 본질입니다. 폼 데이터를 전송하는 테스트 코드는 csrf 반드시 추가해야 합니다.
* 검증
  * 입력값 오류시 동작 -> 폼 화면이 다시 보여지는가
  * 입력값 정상시 동작
    * 회원 저장이 되는가
    * 이메일이 발송되는가
* `Mocking` : 외부 연동을 통한 테스트
  * 인터페이스만 사용하고, 실제로 메일을 발송하는 건 외부 서비스 입니다.
  * Gmail, 네이버 메일 등 메일박스를 열고 꺼내고 실제 데이터를 확인하는 건 유지보수성에 취약함

끝으로, 테스트 코드 비교(초기 vs 수정)하면서 미흡했던 점을 생각해봤습니다. 구멍이 많이 보이는, 의미없는 테스트였네요.

|| target               | action                      |
|-----|----------------------|-----------------------------|
|초기| 회원 저장                | repository에서 직접 데이터를 꺼내와서 비교 |
|초기| 이메일 발송               | 이메일이 발송됐는지 / 발송된 이메일을 꺼내서 비교 |
|수정| POST "url" 입력값 오류 처리 | 다시 폼 화면 보여주기                |
|수정| POST "url" 입력값 정상 처리 | 회원 정보 저장 / 이메일 발송됐는지        |

### 5-3. 리팩토링
* 맥락 (논리적으로 서술되어야 합니다.)
* 각 계층의 본질에 집중하여 너무 많은 책임을 전가하지 않아야 합니다. 
* 읽기 편해야 됩니다.
* 끝으로 리팩토링이 다른 코드에 영향을 주지 않았다는 것을 증명하기 위해 지금까지의 테스트 코드 실행/PASS 확인

### 6. 패스워드 인코딩
보안에 민감한 정보는 양방향(암호화/복호화)를 할 필요가 없습니다. PW는 오직 단방향. 해싱을 하면 된다.
* Configuration passwordEncoder(bcrypt)
* test
  * account 가 null 이 아닌지
  * 저장된 패스워드가 평문 패스워드가 아닌지
  * 입력한 평문이 변환된 해시값과 일치하는지만 체크

### 5-1. Work Flow
* 회원 정보 저장
* 인증 이메일 발송
  * 임시 토큰 발급 - UUID
  * 이메일 발송 (JavaMailSender)


해싱 알고리즘을 쓰는 이유
* 대부분은 유저는 한가지 비밀번호를 여러 사이트에서 사용함 > 엄청 큰 문제임
* PW 자체가 털리는건.. 은행 계좌번호 or 주식 계좌번호 등..
* 해싱 : 문자열을 특정한 보안 알고리즘에 따라서 암호화한다. xf unkasdhfuaweikfhueawkrfhjeoaw 

솔트를 쓰는 이유
* 솔트 : 랜덤 바이트 ( pw+salt => 전혀 다른 값. salt 로 인해서 매번 해시값이 바뀜) 
  * 해커가 dictionary attack. 이미 해시값들을 변환해놓고, PW를 유추함
  * 그럼 어떻게 pw를 찾을 수 있는가?
  * 나온결과에 해시값 자체를 salt 처럼 써서, 다시 해싱ㅂ해버리면 기본 평문이 나옴 > 알고리즘 특성상 원래 해시값이 나옵니다.


Bcrypt 의 경우, 강도가 10 으로 설정함. 강도가 높아질수록 시간이 오래 걸림. 그만큼 해커들이 해킹하는데 힘들다는 얘기.
오히려 느린게 장점인거죠.

코드 접근방법이 완전히 틀림. 솔직히 몰랐고. 찾아봐도 몰랐음. 왜냐하면 AppConfig 로 설정해줘야했거든. 어떨때 AppConfig 로 구분해야하는지 몰랐다는거지


```java
// 나는 진짜 단순하게 service안에서 구현
private Account saveNewAccount(SignUpForm signUpForm) {
  // 대전제: 평문으로 저장하면 안됨
  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  String encodedPassword = encoder.encode(signUpForm.getPassword());
```

```java


```
