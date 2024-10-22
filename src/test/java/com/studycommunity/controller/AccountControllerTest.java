package com.studycommunity.controller;

import com.studycommunity.account.AccountRepository;
import com.studycommunity.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional //TODO: 문제 없으려나
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean // Mock vs MockBean
    JavaMailSender javaMailSender;

    @BeforeEach
    public void clear() {
        accountRepository.deleteAll();
    }

    @DisplayName("회원 가입 화면 보이는지 테스트")
    @Test
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 정보 저장 - 입력값 오류")
    @Test
    public void signUpFormSubmit_with_wrong_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "chandler")
                        .param("email", "email.....")
                        .param("password", "123")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @WithMockUser(username = "chandler", roles = "ROlE_USER")
    @DisplayName("회원 정보 저장 - 입력값 정상일 때 데이터가 존재하는지")
    @Test
    public void signUpFormSubmit_with_right_input_exist_data() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "chandler")
                        .param("email", "chandler@gmail.com")
                        .param("password", "123456789")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());

        assertTrue(accountRepository.existsByEmail("chandler@gmail.com"));
        assertTrue(accountRepository.existsByNickname("chandler"));
        assertEquals(1L, accountRepository.count());
    }

    @WithMockUser(username = "chandler", roles = "ROlE_USER")
    @DisplayName("회원 정보 저장 - 입력값 정상일 때 메일이 보내지는지")
    @Test
    public void signUpFormSubmit_with_right_input_send_email() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "chandler")
                        .param("email", "chandler@gmail.com")
                        .param("password", "123456789")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());

        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }

    @WithMockUser(username = "chandler", roles = "ROlE_USER")
    @DisplayName("회원 정보 저장 - 패스워드 인코드")
    @Test
    public void signUpFormSubmit_with_password_encode() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "chandler")
                        .param("email", "chandler@gmail.com")
                        .param("password", "123456789")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());

        Account account = accountRepository.findByEmail("chandler@gmail.com");
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "123456789");
        assertNotNull(account.getEmailCheckedToken());
        assertTrue(passwordEncoder.matches("123456789", account.getPassword()));
    }

    @DisplayName("인증 메일 확인 - 입력값 오류")
    @Test
    public void checkEmailToken_with_wrong_email() throws Exception {
        mockMvc.perform(get("/check-email-token")
                        .param("email", "email@email.com")
                        .param("token", "rjieowehjirow"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeExists("error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser(username = "chandler", roles = "ROlE_USER")
    @DisplayName("인증 메일 확인 - 입력값 정상")
    @Test
    public void checkEmailToken_with_right_input() throws Exception {
        //given
        Account account = Account.builder()
                .email("chandler@naver.com")
                .nickname("chandler")
                .password("123456789")
                .build();

        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();

        //expected
        mockMvc.perform(get("/check-email-token")
                        .queryParam("email", newAccount.getEmail())
                        .queryParam("token", newAccount.getEmailCheckedToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(authenticated());

        assertTrue(newAccount.isEmailVerified());
        assertNotNull(newAccount.getJoinedAt());
    }

}