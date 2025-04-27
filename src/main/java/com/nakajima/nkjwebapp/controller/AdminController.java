package com.nakajima.nkjwebapp.controller;

import com.nakajima.nkjwebapp.model.Category;
import com.nakajima.nkjwebapp.model.Contact;
import com.nakajima.nkjwebapp.model.UserInfo;
import com.nakajima.nkjwebapp.service.CategoryService;
import com.nakajima.nkjwebapp.service.ContactService;
import com.nakajima.nkjwebapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin")
    public String adminHome() {
        return "admin";
    }

    // アカウント一覧&ページネーション対応
    @GetMapping("/user")
    public String showUserList(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<UserInfo> userPage = userService.getUsersByPage(pageable);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        return "user";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        UserInfo user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit_user";
    }

    @PostMapping("/user/update")
    public String updateUser(
        @RequestParam Integer id,
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String role,
        @RequestParam(required = false) String furigana,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false) Integer age,
        @RequestParam(required = false) String selfIntroduction,
        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
        RedirectAttributes redirectAttributes) {

        String imageFileName = null;
        try {
            imageFileName = userService.saveProfileImage(profileImage);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "画像のアップロードに失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/user";
        }

        userService.updateUser(id, username, email, role, furigana, gender, age, selfIntroduction, imageFileName);
        redirectAttributes.addFlashAttribute("message", "ユーザー情報を更新しました");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/user";
    }

    @PostMapping("/user/create")
    public String createUser(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String role,
        @RequestParam(required = false) String furigana,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false) Integer age,
        @RequestParam(required = false) String selfIntroduction,
        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
        RedirectAttributes redirectAttributes) {

        String imageFileName = null;

        try {
            imageFileName = userService.saveProfileImage(profileImage);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "画像のアップロードに失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/user";
        }

        try {
            userService.createUser(username, email, password, role, furigana, gender, age, selfIntroduction, imageFileName);
            redirectAttributes.addFlashAttribute("message", "ユーザーを追加しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "ユーザーの追加に失敗しました: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/user";
    }

    @GetMapping("/createuser_ad")
    public String showCreateUserForm(Model model) {
        return "createuser_ad";
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
        return "redirect:/user";
    }

    @GetMapping("/contact_ad")
    public String showContactList(Model model) {
        List<Contact> contacts = contactService.getAllContacts();
        model.addAttribute("contacts", contacts);
        return "contact_ad"; // お問い合わせ一覧画面の表示
    }

    // お問い合わせ詳細画面
    @GetMapping("/contact_ad/{id}")
    public String showContactDetail(@PathVariable int id, Model model) {
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "contact_detail";  // 詳細画面の表示
    }

    // お問い合わせステータス更新
    @PostMapping("/contact_ad/update_status")
    public String updateStatus(@RequestParam int id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        contactService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("message", "ステータスが更新されました");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/contact_ad";
    }

    @PostMapping("/contact_ad/save")
    public String saveContact(@Valid @ModelAttribute Contact contact, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "入力に誤りがあります");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/contact_ad";
        }
        contactService.saveContact(contact);
        redirectAttributes.addFlashAttribute("message", "お問い合わせが送信されました");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/contact_ad";
    }

    // カテゴリー作成
    @PostMapping("/admin/category/save")
    public String createCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            categoryService.createCategory(name);
            redirectAttributes.addFlashAttribute("message", "カテゴリを作成しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "カテゴリの作成に失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/category_list";
    }

    // カテゴリーリスト表示
    @GetMapping("/category_list")
    public String showCategoryList(Model model) {
        try {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
        } catch (Exception e) {
            model.addAttribute("message", "カテゴリの取得に失敗しました");
            model.addAttribute("alertClass", "alert-danger");
        }
        return "category_list";  // category_list.htmlを返す
    }

    // カテゴリー削除
    @PostMapping("/category/delete")
    public String deleteCategory(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("message", "カテゴリを削除しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "カテゴリの削除に失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/category_list";
    }

    // カテゴリー編集フォーム表示
    @GetMapping("/category/edit/{id}")
    public String editCategory(@PathVariable Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);  // モデルにカテゴリ情報を追加
            return "edit_category";  // edit_category.htmlを返す
        } else {
            // カテゴリが存在しない場合
            return "redirect:/category_list";  // カテゴリ一覧にリダイレクト
        }
    }


    // カテゴリー編集処理
    @PostMapping("/category/update")
    public String updateCategory(@RequestParam Integer id, @RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(id, name);  // name 引数を追加
            redirectAttributes.addFlashAttribute("message", "カテゴリを更新しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "カテゴリの更新に失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/category_list";
    }

    @GetMapping("/create_category")
    public String showCreateCategoryForm(Model model) {
        return "create_category";  // create_category.html を返す
    }
    
    @GetMapping("/deleted_users")
    public String showDeletedUsers(Model model) {
        List<UserInfo> deletedUsers = userService.getDeletedUsers();
        model.addAttribute("deletedUsers", deletedUsers);
        return "deleted_users";
    }

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
        return "redirect:/user";
    }

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
        return "redirect:/user";
    }

    @PostMapping("/user/lock")
    public String lockUser(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        boolean result = userService.lockUser(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "アカウントをロックしました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        } else {
            redirectAttributes.addFlashAttribute("message", "アカウントのロックに失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/user";
    }

    @PostMapping("/user/unlock")
    public String unlockUser(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        boolean result = userService.unlockUser(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "アカウントのロックを解除しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message", "ロック解除に失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/user";
    }

    @PostMapping("/user/toggle-role")
    public String toggleUserRole(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        boolean result = userService.toggleUserRole(id);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "ユーザーの権限を切り替えました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-info");
        } else {
            redirectAttributes.addFlashAttribute("message", "権限の切り替えに失敗しました");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/user";
    }
}
