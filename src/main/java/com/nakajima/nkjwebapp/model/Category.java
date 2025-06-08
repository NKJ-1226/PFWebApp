package com.nakajima.nkjwebapp.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    @Size(max = 255, message = "カテゴリ名は255文字以内で入力してください")
    private String name; //カテゴリー名

    //Contact.javaとの内部結合
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts;

    @Override
    public String toString() {
        return name != null ? name :"未設定";
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 作成日

    @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
    }    

    @Column(name = "updated_at")
    private LocalDateTime updateAt; // 更新日

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 削除日
        
}
