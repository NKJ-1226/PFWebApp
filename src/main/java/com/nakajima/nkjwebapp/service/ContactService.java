package com.nakajima.nkjwebapp.service;

import com.nakajima.nkjwebapp.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.nakajima.nkjwebapp.repository.ContactRepository;

@Service
@Transactional
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JavaMailSender mailSender;

    // お問い合わせ内容を保存
    public void saveContact(Contact contact) {
        contactRepository.save(contact);
    }

    // ステータスを更新
    public void updateStatus(int id, String status) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        contact.setStatus(status);
        contactRepository.save(contact);
    }

    // お問い合わせ内容を取得
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // お問い合わせの詳細を取得
    public Contact getContactById(int id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }

    // 管理者に通知を送る
    public void sendNotificationToAdmin(Contact contact) {
        try {
            MimeMessage message = mailSender.createMimeMessage(); // MimeMailMessage から MimeMessage に変更
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true はマルチパート対応
            helper.setTo("miyju.nkj@gmail.com"); // 管理者のメールアドレス
            helper.setSubject("新着のお問い合わせあり⇒");
            helper.setText("カテゴリー: " + contact.getCategory() + "\nメッセージ: " + contact.getMessage());

            mailSender.send(message); // メール送信
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
