package com.nakajima.nkjwebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;  // 修正
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.nakajima.nkjwebapp.model.UserInfo;
import com.nakajima.service.UserService;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userForm", new UserInfo());
        return "login"; // ログインページの表示
    }

    @PostMapping("/login")
    public String loginProcess(@ModelAttribute UserInfo userForm, Model model) {
        UserInfo user = userService.findByUsername(userForm.getUsername());
        if (user != null && passwordEncoder.matches(userForm.getPassword(), user.getPassword())) {
            return "redirect:/top";
        } else {
            model.addAttribute("error", "ログインIDまたはパスワードが間違っています。");
            return "login";
        }
    }

    // 認証されていない場合はログイン画面にリダイレクト
    @GetMapping("/")
    public String redirectToTop() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/top";
        }
        return "redirect:/login"; // ログイン画面にリダイレクト
    }

    @GetMapping("/user")
    public String user() {
        return "user"; // ユーザー情報ページの表示
    }
}
