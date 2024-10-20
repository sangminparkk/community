package com.studycommunity.controller;

import com.studycommunity.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

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
                .andExpect(model().attributeExists("signUpForm"));
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
                .andExpect(view().name("account/sign-up"));
    }

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
                .andExpect(redirectedUrl("/"));

        assertTrue(accountRepository.existsByEmail("chandler@gmail.com"));
        assertTrue(accountRepository.existsByNickname("chandler"));
        assertEquals(1L, accountRepository.count());
    }

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
                .andExpect(redirectedUrl("/"));

        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}