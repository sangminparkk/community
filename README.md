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

| 날짜    | 업무                                | 비고                |
|-------|-----------------------------------|-------------------|
| 10/17 | entity / controller / view / test | 여러테이블과의 연관관계 맵핑 X |
| 10/18 | form submit validation            |                   |
| 10/19 |                                   |                   |
| 10/20 |                                   |                   |


## Things I learned
새롭게 알게 된 내용을 빠짐없이 기재하기 바랍니다.

### code
* public method : 읽기 편하게 짜야 합니다. 디테일한 구현은 extract 기능으로 잡아줍니다.

### spring
* model.addAttribute : 객체만 생성해서 넘겨도 spring MVC에서 자동으로 camelCase로 변환해서 뷰로 전달합니다.

### view
* Image 추가 : images 패키지에 저장하고, 정적 리소스에 대한 시큐리티 설정이 필요합니다. (web.ignoring)
* root page 설정 : resources/templates-index.html 을 기본적으로 루트 페이지로 설정함 (이걸 몰랐냐..심각하다)

### tools
* 부트스트랩 설정
* dev-tools : 서버를 껐다가 켜지 않아도 build를 통해서 뷰 페이지 변경사항을 적용해줍니다.

