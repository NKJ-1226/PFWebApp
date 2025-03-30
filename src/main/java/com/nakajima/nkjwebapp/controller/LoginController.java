package com.nakajima.nkjwebapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nakajima.nkjwebapp.model.UserInfo;

@Controller
@RequestMapping
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userForm", new UserInfo());
        return "login"; // ログインページの表示
    }

    // 認証されていない場合はログイン画面にリダイレクト
    @GetMapping("/")
    public String redirectToTop() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            UserInfo user = (UserInfo) authentication.getPrincipal();
            return user.isAdmin() ? "redirect:/admin" :"redirect:/top"; // 管理者か一般ユーザーか判別する
        }
        return "redirect:/login"; // ログイン画面にリダイレクト
    }

    @GetMapping("/top")
    public String top() {
        return "top"; // 一般ユーザー用画面の表示
    }

}