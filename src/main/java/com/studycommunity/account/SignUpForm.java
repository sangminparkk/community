package com.studycommunity.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SignUpForm {

    @Email
    @NotBlank(message = "이메일을 입력하세요")
    private String email;

    //TODO: 둘중 하나만 체크해도 되지 않을까? 체크해보기
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;

    @Length(min = 8, max = 50)
    @Pattern(regexp = "^[a-z0-9_!@-]+$")
    @NotBlank(message = "패스워드를 입력하세요.")
    private String password;

}
