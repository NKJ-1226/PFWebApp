package com.nakajima.nkjwebapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nakajima.nkjwebapp.model.UserInfo;
import com.nakajima.nkjwebapp.service.UserService;

@Controller
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 他ユーザーの一覧表示
    @GetMapping("/user_list")
    public String listUsers(Model model) {
        List<UserInfo> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user_list"; // templates/user_list.html が必要
    }

    // 他ユーザーのプロフィール表示
    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable Integer id, Model model) {
        UserInfo user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user_profile"; // templates/user_profile.html が必要
    }

    // 他ユーザーのプロフィール詳細画面表示
    @GetMapping("/detail/{id}")
    public String userDetail(@PathVariable Integer id, Model model, @AuthenticationPrincipal UserInfo currentUser) {
        UserInfo user = userService.findById(id);
        int likeCount = userService.getLikeCount(id); // いいねのカウントを取得
        boolean hasLiked = userService.hasUserLiked(currentUser.getId(), id);  // 現在のユーザーがすでに「いいね」しているか確認
        model.addAttribute("user", user);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("hasLiked", hasLiked);
        return "user_detail"; 
    }

    // いいね処理
    @PostMapping("/{id}/like")
    public String likeUser(@PathVariable Integer id, @AuthenticationPrincipal UserInfo currentUser) {
        userService.likeUser(currentUser.getId(), id);
        return "redirect:/detail/" + id;
    }

    // 月間いいね数でランキング表示
    @GetMapping("/like_ranking")
    public String getMonthlyRankings(Model model) {
        List<UserInfo> rankedUsers = userService.getUserRankedByLikesThisMonth();
        model.addAttribute("rankedUsers", rankedUsers);
        return "like_ranking"; 
    }
}
