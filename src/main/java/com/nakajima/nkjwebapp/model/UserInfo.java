package com.nakajima.nkjwebapp.model;

import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users") // データベースのテーブル名の設定
@NoArgsConstructor  // デフォルトコンストラクタを作成
@AllArgsConstructor // すべてのフィールドを引数に持つコンストラクタを自動生成
@Data // ゲッター・セッター・toStringを自動生成
public class UserInfo implements UserDetails {
    
    @Id // IDを主キーに設定
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動採番
    private int id;

    @Column(nullable = false, unique = true, length = 255) // nullを許可するかどうか、最大255文字
    @Size(max = 255, message = "255文字以内で入力してください")
    private String username;

    @Pattern(regexp = "^$|^[\\u3040-\\u309Fー]+$", message = "ひらがなのみで入力してください")
    @Column(name = "furigana", length = 255)
    private String furigana;

    @Size(min = 8, max = 32) // 8~32文字
    @Pattern(regexp = "^[a-zA-Z0-9_-]{8,32}$", message = "8~32文字の半角英数字と_-のみ許可") // パスワードに許可する文字を指定
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true) // unique...重複禁止
    private String email;

    @Column(nullable = false) 
    private String role; 

    @Column(name = "profile_image") // プロフィール画像
    private String profileImage;

    @Column(name = "gender", length = 10) // 性別(男性、女性など)
    private String gender;

    @Column(name = "age") // 年齢
    private Integer age;

    @Size(max = 1500, message = "自己紹介は1500文字以内で入力してください") // 自己紹介（最大1500文字）
    @Column(name = "self_introduction", length = 1500)
    private String selfIntroduction;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 論理削除フラグ（デフォルト false）

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked = false; // アカウントロック（アクセス禁止）フラグ

    private int likeCountThisMonth;
    
    private int likeCountThisYear;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createAt; // アカウント作成日

    @Column(name = "updated_at")
    private LocalDateTime updateAt; // アカウント更新日

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // アカウント削除日


// getter / setter を追加

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    // 管理者権限を持っているか判定
    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);
    }
    
    // アカウントの削除確認
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // アカウントがロックされていないか（アクセス可能か）
    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ユーザーが有効かどうか（論理削除されていたら無効にする）
    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }

    @Transient
    private int likeCount;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

}