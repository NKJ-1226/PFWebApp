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

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true) //unique...ダブり禁止するかしないか
    private String email;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //必ずこの変数にすること！！
        return List.of(new SimpleGrantedAuthority(role));
        
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
