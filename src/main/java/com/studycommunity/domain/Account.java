package com.studycommunity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor //TODO: 필요한 이유는? 굳이 필요할까? 체크해보기
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    // email verified / token(검증시)
    private boolean emailVerified;

    private String emailCheckedToken;

    // 인증 거친 대상자들이 가입한 날짜
    private LocalDateTime joinedAt;

    //TODO: 객체로 던지는 거 vs 데이터 나열해서 저장하는거 장단점 고민해보기
    private String bio; // 짧은 자기소개

    private String url;

    private String occupation;

    private String location;

    @Lob @Basic
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    //TODO: 어디에 쓰이는지 머리에 떠오르지 않음. 구현때 다시 필요한 이유에 대해서 고민해볼것
    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    public void generateEmailCheckToken() {
        this.emailCheckedToken = UUID.randomUUID().toString();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckedToken.equals(token);
    }
}
