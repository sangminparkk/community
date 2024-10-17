package com.studycommunity.account;

import com.studycommunity.request.AccountForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("accountForm", new AccountForm());
        return "account/sign-up";
    }

}
