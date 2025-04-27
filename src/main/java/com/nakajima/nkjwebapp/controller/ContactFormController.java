package com.nakajima.nkjwebapp.controller;

import com.nakajima.nkjwebapp.model.Contact;
import com.nakajima.nkjwebapp.repository.CategoryRepository;
import com.nakajima.nkjwebapp.model.Category; 
import com.nakajima.nkjwebapp.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String submitContactForm(Contact contact, Model model) {
        // CategoryのIDを使用して、Categoryを取得
        Category category = categoryRepository.findById(contact.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // CategoryをContactにセット
        contact.setCategory(category);

        // お問い合わせ内容を保存
        contactService.saveContact(contact);

        // 管理者に通知
        contactService.sendNotificationToAdmin(contact);

        return "redirect:/thank_you";
    }
}
