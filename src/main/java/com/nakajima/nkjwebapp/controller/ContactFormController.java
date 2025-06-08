package com.nakajima.nkjwebapp.controller;

import com.nakajima.nkjwebapp.model.Contact;
import com.nakajima.nkjwebapp.repository.CategoryRepository;
import com.nakajima.nkjwebapp.model.Category; 
import com.nakajima.nkjwebapp.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactFormController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private CategoryRepository categoryRepository;

    // お問い合わせフォームを表示
    @GetMapping("/contact_form")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());

        // カテゴリのリストを取得してModelに追加
        model.addAttribute("categories", categoryRepository.findAll());

        return "contact_form";
    }

    // お問い合わせフォーム送信時
    @PostMapping("/contact_form")
    public String submitContactForm(Contact contact, RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(contact.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        contact.setCategory(category);

        contactService.saveContact(contact);
        contactService.sendNotificationToAdmin(contact);

        // フラッシュメッセージを設定
        redirectAttributes.addFlashAttribute("message", "お問い合わせを送信しました。");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // ログイン画面にリダイレクト
        return "redirect:/login";
    }
}
