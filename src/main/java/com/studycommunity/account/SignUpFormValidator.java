package com.studycommunity.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) object;
        checkExistsByEmail(errors, signUpForm);
        checkExistsByNickname(errors, signUpForm);
    }

    private void checkExistsByNickname(Errors errors, SignUpForm signUpForm) {
        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpForm.getNickname()}, "이미 존재하는 닉네임 입니다.");
        }
    }

    private void checkExistsByEmail(Errors errors, SignUpForm signUpForm) {
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 존재하는 이메일 입니다.");
        }
    }
}
