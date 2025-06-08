package com.nakajima.nkjwebapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // Categoryエンティティとの紐付け！

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

    @Column(nullable = false)
    private boolean deleted =false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 作成日    

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }    
}
