# 프로젝트로 배우는 스프링
reference : [커리큘럼 스프링과 JPA 기반 웹 애플리케이션 개발, 백기선](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-JPA-%EC%9B%B9%EC%95%B1/dashboard)
* 하게 된 이유 : 현업에서 막막하게 개발해야하는 상황을 미리 체험하고 극복하기 위해 어떤 노력이 더 필요한지 가감없이 느껴보기 위함
* 프로젝트 시작일 : 10/16, 9:40PM 
* 프로젝트 종료일 : tbd

## 개발환경
* maven
* java 17
* spring boot 3.3.4
* library
  * spring web
  * spring-boot-devtools
  * spring security
  * h2 database
  * postgresql
  * lombok
  * thymeleaf
  * mail-sender
  * queryDSL

## Git Branch 전략
* main(=develop)
  * feature1, feature2, ,,, 

## Things to think about
현업에서 커뮤니티 서비스를 배포해야하는 상황이라고 가정하고, 내가 어떤 부분에 집중해서 개발해야하는지 체크해봅니다.
* work-flow 를 생각하면서 업무를 해야하는데 어떻게 하면 큰틀에서 접근할 수 있을까? (고민중)

## Things to do
부족하다고 생각되는 개념이나 잘못 알고 있었던 모든 것들을 빠짐없이 기재하기 바랍니다. 그리고 채워넣길 바랍니다.
* 

## Things I am doing
배포일정 고려해서, 어떤 업무별로 시간이 많이 걸리는지 체크합니다. 향후 업무에 도움되길 바랍니다.
* 강의시간 : 18시간 12분
* 업무시간(*2) : 4.5일(=36시간) -> 일주일만에 끝내는 걸 목표로 얼마나 힘든지도 경험해봅니다.

| 날짜    | 업무                                         | 비고                                                                  |
|-------|--------------------------------------------|---------------------------------------------------------------------|
| 10/17 | entity / controller / view / test          | 여러테이블과의 연관관계 맵핑 X                                                   |
| 10/18 | form submit validation                     | DB 매칭 필요하므로 굉장히 중요                                                  |
| 10/19 | form submit process - business logic       | 임시토큰발급/인증 이메일 송부                                                    |
| 10/20 | form submit test code / passwordEncoder    | 테스트 코드 작성이 불가능한 현재 수준                                               |
| 10/21 | check email and token to complete sign up  | 인증 이메일을 통한 회원 가입 완료                                                 |
| 10/22 | login process                              | springContextHolder를 통한 자동로그인                                       |
| 10/23 | na                                         | 풀리지 않는 이슈 때문에 멘탈이 나감                                                |
| 10/24 | na                                         | ↑                                                                   |
| 10/25 | na                                         | ↑                                                                   |
| 10/26 | login / logout supported by spring security | SecurityConfig를 통한 로그인/로그아웃 설정                                      |
| 10/27 | na                                         | 쉬었음                                                                 |
| 10/28 | 개발 중단                                      | spring security에 대한 이해도 부족으로 프로젝트 추가 진행 불가. 관련 지식 빠르게 습득 후 다시 개발 필요 |


## Things I learned
새롭게 알게 된 내용을 빠짐없이 기재하기 바랍니다. 아래 카테코리 기준으로 정리했습니다.
* code
* spring
* view
* test code(추가)
* performance
* tools
* tips

## code
* public method : 읽기 편하게 짜야 합니다. 디테일한 구현은 extract 기능으로 잡아줍니다.

## spring
* model.addAttribute
  * 객체만 생성해서 넘겨도 spring MVC에서 자동으로 camelCase로 변환해서 뷰로 전달합니다.
  * @ModelAttribute : 복잡 객체를 파라미터로 받을때 생략해도 된다.
* 데이터 유효성 검사 & 검증기 설정(`@InitBinder`)
  * @Valid
    * @Pattern : 정규식 패턴을 적용할 수 있고 기본적으로 문자열 필드에만 적용 가능하며 다른 데이터 타입에서는 사용이 제한됩니다.
  * Custom 검증([Validator](https://docs.spring.io/spring-framework/reference/core/validation/validator.html)) : 비지니스 로직에 따른 복잡한 검증 (DB와 매칭)
* `@Transactional`
  * 데이터베이스 작업을 하나의 단위로 묶어서 처리할 때, 하나의 트랜잭션 범위 내에서 수행될 수 있도록 합니다. 에러 발생시 롤백 기능을 통해 데이터 안전성을 확보합니다.
  * 적용케이스
    * repository : 단순 조회인 경우, 성능 최적화를 위해 사용하는 경우가 있습니다.
    * service : 비지니스 로직을 처리하면서 데이터 수정/삭제/추가 등 JPA 매커니즘(변경감지로직)에 의해 수행
* **토큰 발급**
  * 토큰은 평문으로 발급 받는 순간, 고객 정보가 유출될 수 있으므로 항상 조심해야 합니다.
  * UUID를 사용해서 개발을 빠르게 진행할 수 있지만, 보안관점에서 고도화된 알고리즘으로 변경을 반드시 해야 합니다.
* JavaMailSender
  * SimpleMailMessage(수신자/제목/내용 설정)를 활용하여 메시지 생성 후 이메일 송부 기능을 지원합니다.
* `GET Mapping` 
  * 기본적으로 view 렌더링에 목적이 있으며, model 객체에 데이터 바인딩 후 html에서 해당 데이터를 사용할 수 있습니다.
  * 쿼리 파라미터를 받는 경우, `@RequestParam`을 생략할 수 있습니다.
  * model.addAttribute() 를 통해 error를 넘길 수도 있고, 필드값을 넘길 수도 있습니다.
  * 최종적으로 뷰 템플릿을 반환합니다. (ex. sign-up.html)
* `@WithMockUser`
  * Spring security가 적용된다면 테스트 환경에서 지원하는 기능 중 하나로 테스트 환경에서 SecurityContextHolder에 사용자 인증 정보를 자동으로 설정해줍니다.
  * 인증된 사용자를 모킹해서 테스트를 수행하고, 결과적으로 인증이 필요한 테스트에 한해서 해당 어노테이션을 추가해주면 됩니다.


## view
* Image 추가 : images 패키지에 저장하고, 정적 리소스에 대한 시큐리티 설정이 필요합니다. (web.ignoring)
* root page 설정 : resources/templates-index.html 을 기본적으로 루트 페이지로 설정함 (이걸 몰랐냐..심각하다)
* 입력값 오류 시 alert-danger
* 텍스트 중간에 글자 혹은 숫자를 끼워넣고 싶은 경우, `span` 태그 사용
  * <span th:text="${number}"></span>


## test code
테스트 코드를 잘 짜기 위한 기본 전제는 **개발한 로직에 대한 이해도**가 높아야 합니다.  
로직을 차례대로 따라가면서 unit 테스트로 **최대한 빈틈없게 작성**해야 합니다.  
테스트 성공 뿐만 아니라 실패가 발생하는 상황(메인 로직 주석처리)도 연출하여 테스트합니다.

* `csrf` : 폼 데이터를 전송하는 테스트시 반드시 고려해야 합니다.
  * 인증되지 않은 사용자 접근을 받아들이되, 안전하지 않은 요청까지 받아들일 필요는 없습니다.
  * spring security + thymeleaf 사용시, hidden csrf token default 적용됩니다.
* `Mocking` : 외부 연동을 통한 테스트
  * 인터페이스만 사용하고, 실제로 메일을 발송하는 건 외부 서비스 입니다.
  * Gmail, 네이버 메일 등 메일박스를 열고 꺼내고 실제 데이터를 확인하는 건 유지보수성에 취약하므로 호출됐는지만 체크합니다.

|| target               | action                       |
|-----|----------------------|------------------------------|
|초기| 회원 저장                | repository에서 직접 데이터를 꺼내와서 비교 |
|초기| 이메일 발송               | 이메일이 발송됐는지 / 발송된 이메일을 꺼내서 비교 |
|수정| POST "url" 입력값 오류 처리 | 다시 폼 화면 보여주기                 |
|수정| POST "url" 입력값 정상 처리 | 회원 정보 저장 / 이메일 발송 여부(외부 서비스) |


## Performance(성능)
* Repository 단순 조회시 : **@Transactional(readOnly = true)** 적용
  * 성능 최적화(단순 조회의 경우, 변경 감지 로직 처리를 하지 않음) + 명확한 의도 전달

## tools
* 부트스트랩 설정
* dev-tools : 서버를 껐다가 켜지 않아도 build를 통해서 뷰 페이지 변경사항을 적용해줍니다.
* `@Profile` : yml파일에 프로파일 액티브 설정해주면, 해당 어노테이션 적용된 클래스는 지정된 프로파일로 동작할때 스프링 빈에 등록되고 관리됩니다.

## tips
* 메서드 네이밍
  * signUp -> signUpSubmit
* 로그 찍어서 눈으로 확인하는 습관(@Slf4j)
  * 테스트하기 전에 해당 기능이 잘 동작하는지 항상 로그를 찍어볼 것
* 리팩토링
  * 맥락 (논리적으로 서술되어야 합니다.)
  * 각 계층의 본질에 집중하여 너무 많은 책임을 전가하지 않아야 합니다.
  * 끝으로 리팩토링이 다른 코드에 영향을 주지 않았다는 것을 증명하기 위해 지금까지의 테스트 코드 실행/PASS 확인

## Issue
* No1 : redirect 이후 authority 변경되어 뷰 템플릿이 출력이 안되는 문제
  * 원인 : ROLE_USER > ROLE_ANONYMOUS
  * 상황 : 하기 캡처
![img_1.png](img_1.png)
  * 해결 : 아직 찾지 못함(3 days..)
    * 시큐리티 설정에 의한 문제가 발생한 것으로 판단되며, API 문서를 정독해보고 문제를 찾아야할듯. 시간을 너무 많이 소요함..