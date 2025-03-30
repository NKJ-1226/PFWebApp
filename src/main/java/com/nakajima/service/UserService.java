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

    // 削除されていない全ユーザーを取得
    public List<UserInfo> getAllUsers() {
        return userRepository.findByIsDeletedFalse();
    }

    // 削除済みのユーザーを取得
    public List<UserInfo> getDeletedUsers() {
        return userRepository.findByIsDeletedTrue();
    }

    // 削除されていないユーザーを ID で取得
    public UserInfo getUserById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleted()) // 削除済みなら除外
                .orElse(null);
    }

    // 削除されていないユーザーを username で取得
    public UserInfo findByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username).orElse(null);
    }

    // ユーザー情報を更新
    @Transactional
    public void updateUser(Integer id, String username, String email) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        if (user.isDeleted()) {
            throw new RuntimeException("削除されたユーザーは更新できません");
        }

        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
    }

    // ユーザーを論理削除
    @Transactional
    public boolean deleteOne(Integer id) {
        try {
            UserInfo user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

            if (user.isDeleted()) {
                throw new RuntimeException("このユーザーはすでに削除されています");
            }

            user.setDeleted(true); // 論理削除フラグを true に
            userRepository.save(user); // 更新

            return true; // 削除成功
        } catch (Exception e) {
            System.err.println("削除中にエラーが発生しました: " + e.getMessage());
            return false; // 削除失敗
        }
    }

    // 削除済みユーザーの復元
    @Transactional
    public boolean restoreUser(Integer id) {
        try {
            UserInfo user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

            if (!user.isDeleted()) {
                throw new RuntimeException("このユーザーは削除されていません");
            }

            user.setDeleted(false); // 論理削除フラグを false に戻す
            userRepository.save(user); // 更新

            return true; // 復元成功
        } catch (Exception e) {
            System.err.println("復元中にエラーが発生しました: " + e.getMessage());
            return false; // 復元失敗
        }
    }

    // ユーザーを物理削除
    @Transactional
    public boolean deleteUserPhysically(Integer id) {
        try {
            UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
            
                userRepository.delete(user); // データベースからユーザーを削除

                return true; // 削除成功
        } catch (Exception e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
            return false; // 削除失敗
        }
    }

}