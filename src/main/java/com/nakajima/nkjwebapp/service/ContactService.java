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
        Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id)); // ここ
        contact.setStatus(status);
        contactRepository.save(contact);
    }

    // お問い合わせ内容を取得
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // お問い合わせの詳細を取得
    public Contact getContactById(int id) {
        return contactRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id)); // ここ
    }

    // 管理者に通知を送る
    public void sendNotificationToAdmin(Contact contact) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true はマルチパート対応

            // 管理者のメールアドレスを環境変数から取得
            String mailTo = System.getenv("SPRING_MAIL_USERNAME");
            helper.setTo(mailTo); // 管理者のメールアドレス
            helper.setSubject("新着のお問い合わせあり⇒");

            // カテゴリ名を取得
            if (contact.getCategory() == null) {
                System.out.println("カテゴリはnullです。");
            } else {
                System.out.println("カテゴリID: " + contact.getCategory().getId() + ", カテゴリ名: " + contact.getCategory().getName());
            }

            // カテゴリ名を取得（未設定の場合を考慮）
            String categoryName = (contact.getCategory() != null && contact.getCategory().getName() != null)
                    ? contact.getCategory().getName()
                    : "未設定"; // カテゴリが未設定の場合

            System.out.println("カテゴリー名: " + categoryName);  // ログ出力を追加

            // メール本文を作成
            String messageText = "カテゴリー: " + categoryName + "\n" +
                                 "メッセージ: " + contact.getMessage();

            helper.setText(messageText);

            // メール送信
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
