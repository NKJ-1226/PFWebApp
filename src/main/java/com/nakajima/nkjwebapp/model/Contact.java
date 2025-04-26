package com.nakajima.nkjwebapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "カテゴリーは必須です")
    @Size(max = 255, message = "カテゴリーは最大255文字までです")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "メッセージは必須です")
    @Size(max = 1000, message = "メッセージは最大1000文字までです")
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String status = "未対応"; // デフォルトは「未対応」

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    @Column(nullable = false)
    private String email; // お問い合わせの送信者メールアドレス
}
