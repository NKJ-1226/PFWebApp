package com.nakajima.nkjwebapp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nakajima.nkjwebapp.model.UserInfo;
import com.nakajima.nkjwebapp.service.UserService;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    // プロフィールページ表示
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserInfo userInfo, Model model) {
        // ログイン中のユーザー情報を取得
        UserInfo user = userService.findByUsername(userInfo.getUsername());
        model.addAttribute("user", user);
        return "profile";
    }

    // プロフィール編集画面を表示
    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal UserInfo userInfo, Model model) {
        UserInfo user = userService.findByUsername(userInfo.getUsername());
        model.addAttribute("user", user);
        return "edit_profile";
    }

    // プロフィール編集内容を保存
    @PostMapping("/profile/edit")
    public String updateProfile(
        @AuthenticationPrincipal UserInfo userInfo,
        @ModelAttribute UserInfo formUser,
        @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
        Model model
    ) throws IOException {
        // プロフィール画像の保存処理
        String savedProfileImage = userService.saveProfileImage(profileImageFile);

        // ユーザー情報を更新
        userService.updateUser(
            userInfo.getId(),
            formUser.getUsername(),
            userInfo.getEmail(),
            userInfo.getRole(),
            formUser.getFurigana(),
            formUser.getGender(),
            formUser.getAge(),
            formUser.getSelfIntroduction(),
            savedProfileImage != null ? savedProfileImage : userInfo.getProfileImage()
        );

        // 更新後のユーザー情報を取得し、セッション内の認証情報を更新
        UserInfo updatedUser = userService.findById(userInfo.getId());
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
            updatedUser,
            updatedUser.getPassword(),
            updatedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/profile";
    }
}
