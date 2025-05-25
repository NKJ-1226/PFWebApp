package com.nakajima.nkjwebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import com.nakajima.nkjwebapp.service.UserService;
import com.nakajima.nkjwebapp.model.UserInfo;

@Controller
@RequestMapping
public class LoginController {

    private final UserService userService;


    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/login")
    // public String login(@RequestParam(value = "error", required = false) String error, Model model) {
    //     if (error != null) {
    //         model.addAttribute("loginError", true);
    //     }
    //     return "login";
    // }
    
    @GetMapping("/login")
    public String login(
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "locked", required = false) String locked,
        Model model) {

        if (error != null) {
            model.addAttribute("loginError", true);
        }
        if (locked != null) {
            model.addAttribute("lockedError", true);
        }

        return "login";
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
    public String showTopPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
            && authentication.getPrincipal() instanceof UserInfo) {

            UserInfo currentUser = (UserInfo) authentication.getPrincipal();

            int monthlyLikes = userService.getTotalLikesThisMonth(currentUser.getId());
            int yearlyLikes = userService.getTotalLikesThisYear(currentUser.getId());

            model.addAttribute("thisMonthLikes", monthlyLikes);
            model.addAttribute("thisYearLikes", yearlyLikes);
            model.addAttribute("username", currentUser.getUsername());
        }

        List<UserInfo> rankedUsers = userService.getUserRankedByLikesThisMonth();
        model.addAttribute("rankedUsers", rankedUsers);

        return "top";
    }

}