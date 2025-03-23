package com.nakajima.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nakajima.nkjwebapp.model.UserInfo;
import com.nakajima.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    // 全ユーザーの情報を取得する
    public List<UserInfo> getAllUsers() {
        return userRepository.findAll();
    }

    // ユーザーをidで取得する
    public UserInfo getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // ユーザーをusernameで取得する
    public UserInfo findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // ユーザー情報を更新する
    @Transactional
    public void updateUser(Integer id, String username, String email) {
        UserInfo user = userRepository.findById(id.intValue()) 
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user); // データベースに保存
    }

    // ユーザーの削除
    @Transactional
    public boolean deleteOne(Integer id) {
    try {
        UserInfo user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        
        userRepository.delete(user); // ユーザーを削除

        // 削除成功
        return true;
    } catch (Exception e) {
        System.err.println("削除中にエラーが発生しました: " + e.getMessage());
        
        // 削除失敗
        return false;
    }
}

}
