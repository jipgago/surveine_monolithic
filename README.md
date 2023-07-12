# surveine_monolithic
Surveine Service Monolithic

**개발 기간** 2023.03 ~ 2023.05 <br/>
**Team repo** https://github.com/orgs/KEA-Kafeine/repositories <br/>

# 😺 Stacks
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white"/> <img src="https://img.shields.io/badge/Springsecurity-6DB33F?style=for-the-badge&logo=Springsecurity&logoColor=white"/> <img src="https://img.shields.io/badge/postgresql-336791?style=for-the-badge&logo=postgresql&logoColor=white"> <img src="https://img.shields.io/badge/mysql-3e6e93?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/JSON-000000?style=for-the-badge&logo=json&logoColor=white"/>

# 📚 Project Structure

```
src
├── config           # 설정파일
├── controller       # 컨트롤러
├── domain           # JPA
├── dto              # 데이터 전달
│   ├── abox         
│   ├── ans
│   ├── cbox
│   ├── enq
│   ├── member
│   └── sbox
├── enum             # 상태 상수
├── repository       # 데이터베이스 컨트롤러
├── security         # 보안
└── service          # 서비스
```

## Components

- config
    - 기본 설정파일
- controller
    - API 통신 정의
- domain
    - 데이터베이스 정의
- dto
    - 데이터 전송 객체파일
- enum
    - 상태 상수 저장 파일
- repository
    - service 안에서 DB Transaction 해주는 JPA Repository
- security
    - JWT Filter 등 보안 관련된 파일들
- service
    - 설문조사 서비스 관련 파일로 구성

# 🙂 Pattern & Workflow

## Repository Pattern

비즈니스 로직이 있는 서비스 계층과 데이터베이스에 접근하는 데이터 소스 계층 사이에 레포지토리 계층을 생성해 두 계층을 중계

## RDB → Key-Value
**ObjectMapper 전략**
(com.fasterxml.jackson.databind.ObjectMapper)

ObjectMapper로 Key-Value 형식으로 저장 및 불러오기
```java
public static String setAnsCont(List<AnsContDTO> ansContDTO) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(ansContDTO);
}

public static List<AnsContDTO> getAnsCont(Ans answer) throws JsonProcessingException {
    if (answer.getCont() == null) {
        return Collections.emptyList();
    } else {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(answer.getCont(), new TypeReference<List<AnsContDTO>>() {
        });
    }
}
```

## JWT를 이용한 Security 전략
```java
  public TokenProvider(@Value("${jwt.secret}") String secretKey) {
      byte[] keyBytes = Decoders.BASE64.decode(secretKey);
      this.key = Keys.hmacShaKeyFor(keyBytes);
  }
```
- Token 방식의 인증을 사용하여 헤더'Bearer'로 토큰을 넘겨줌

# ✨ Installation

## application.yml

```yaml
server:
  port: 8080

spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: #mailID
    password: #mailPassword
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
    transport:
      protocol: smtp
    debug: true
    default-encoding: utf-8
  web:
  # resources:
  datasource:
    url: jdbc:postgresql://localhost:${port}/surveinedb
    username: #db user
    password: #db password
  jpa:
    hibernate:
      ddl-auto: update
    generateddl: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: true
  # Swagger 관련 설정 (오류 방지를 위한 코드)
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
jwt:
  secret: #Secret Key
```