package com.nakajima.nkjwebapp.controller;

import com.nakajima.nkjwebapp.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nakajima.nkjwebapp.service.ContactService;

@Controller
public class ContactFormController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact_form")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact_form";
    }

    @PostMapping("/contact_form")
    public String submitContactForm(@ModelAttribute Contact contact, Model model) {
        contactService.saveContact(contact); // データベース保存処理
        contactService.sendNotificationToAdmin(contact); //メール通知
        model.addAttribute("successMessage", "お問い合わせありがとうございます！");
        return "contact_form";
    }
}
