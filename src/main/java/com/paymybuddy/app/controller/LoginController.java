package com.paymybuddy.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public String getLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/logout")
    @ResponseStatus(code = HttpStatus.OK)
    public String logoutPage(Model model) {
        return "login";
    }
}
