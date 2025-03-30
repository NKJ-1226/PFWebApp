package com.nakajima.nkjwebapp.controller;

import com.nakajima.service.UserService;
import com.nakajima.nkjwebapp.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String adminHome() {
        return "admin"; // admin.htmlを表示させる
    }

    @GetMapping("/user")
    public String showUserList(Model model) {
        // 全ユーザーの情報を取得
        List<UserInfo> users = userService.getAllUsers();
        model.addAttribute("users", users); // ユーザー情報をビューに渡す
        return "user"; // user.htmlを表示させる
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        UserInfo user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit_user";
    }

    @PostMapping("/user/update")
    // ユーザーの更新情報を取得
    public String updateUser(@RequestParam Integer id, @RequestParam String username, @RequestParam String email) {
        userService.updateUser(id, username, email);
        return "redirect:/user"; // 更新後、ユーザー一覧へリダイレクト
    }

    @PostMapping("/user/delete")
    public String postUserDetailDelete(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
    boolean result = userService.deleteOne(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "ユーザーを削除しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success"); 
        } else {
            redirectAttributes.addFlashAttribute("message", "ユーザーの削除に失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger"); 
        }
    return "redirect:/user"; // 削除後、ユーザー一覧へリダイレクト
    }

    @GetMapping("/contact_ad")
    public String showContactList() {
        return "contact_ad"; // contact_ad.htmlを表示させる
    }

    @GetMapping("/deleted_users")
    public String showDeletedUsers(Model model) {
        List<UserInfo> deletedUsers = userService.getDeletedUsers();
        model.addAttribute("deletedUsers", deletedUsers);
        return "deleted_users"; // 削除済みユーザー一覧ページ
    }

    // ユーザーの復元処理
    @PostMapping("/user/restore")
    public String restoreUser(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        boolean result = userService.restoreUser(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "ユーザーを復元しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message", "ユーザーの復元に失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");   
        }
        return "redirect:/user"; // 復元後、ユーザー一覧へリダイレクト
    }

    // ユーザーの物理削除処理
    @PostMapping("/user/delete/physical")
    public String deleteUserPhysically(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        boolean result = userService.deleteUserPhysically(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "ユーザーを物理削除しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message", "ユーザーの物理削除に失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/user"; // 削除後、ユーザー一覧へリダイレクト
    }
}