package com.nakajima.nkjwebapp.model;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") //データベースのテーブル名の設定
@NoArgsConstructor  //デフォで引数いらずのコンストラクタを作成
@AllArgsConstructor //すべてのフィールドを引数の受け取るコンストラクタを自動で作成
@Data //クラスを付与することで全フィールドでゲッターセッターが使える
public class UserInfo implements UserDetails{
    @Id //IDを主キーに設定する
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自動採番する
    private int id;

    @Column(nullable = false) //nullable...nullを許可するかどうか
    private String username;

    @Size(min = 8,max = 32) // 8~32文字以上
    @Pattern(regexp = "^[a-zA-Z0-9_-]{8,32}$",message = "8~32文字の半角英数字と_-のみ許可") // パスワードに許可する文字を指定
    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true) //unique...ダブり禁止するかしないか
    private String email;

    @Column(nullable = false) 
    private String role; 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    // 管理者権限を持っているか判定する
    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);  // roleがROLE_ADMINならば管理者
    }
    
    // アカウントの削除確認
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // アカウントがロックされていないかBAN等の処理に用いる(falseでロック)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
