package com.nakajima.nkjwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/admin") // すべての管理者ページは "/admin" 配下に統一
public class AdminController {

    @GetMapping("/admin")
    public String adminHome() {
        return "admin"; // admin.htmlを表示させる
    }

    @GetMapping("/user")
    public String showUserList() {
        return "user"; // user.htmlを表示させる
    }

    @GetMapping("/contact_ad")
    public String showContactList() {
        return "contact_ad"; // contact_ad.htmlを表示させる
    }
}
