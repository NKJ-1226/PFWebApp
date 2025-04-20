package com.nakajima.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nakajima.nkjwebapp.model.UserInfo;
import com.nakajima.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


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

    // ユーザーの追加
    @Transactional
    public void createUser(String username, String email, String password, String role,
                       String furigana, String gender, Integer age,
                       String selfIntroduction, String profileImage) {

        UserInfo newUser = new UserInfo();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);
        newUser.setDeleted(false);
        newUser.setLocked(false);
        newUser.setFurigana(furigana);
        newUser.setGender(gender);
        newUser.setAge(age);
        newUser.setSelfIntroduction(selfIntroduction);
        newUser.setProfileImage(profileImage);

        userRepository.save(newUser);
    }


    // ユーザー情報を更新
    @Transactional
    public void updateUser(Integer id, String username, String email, String role,
                            String furigana, String gender, Integer age,
                            String selfIntroduction, String profileImage) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        if (user.isDeleted()) {
            throw new RuntimeException("削除されたユーザーは更新できません");
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setFurigana(furigana);
        user.setGender(gender);
        user.setAge(age);
        user.setSelfIntroduction(selfIntroduction);
        if (profileImage != null && !profileImage.isEmpty()) {
        user.setProfileImage(profileImage);
        }
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

    // アカウントをロック（アクセス禁止）
    @Transactional
    public boolean lockUser(Integer id) {
        try {
            UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

            if (user.isLocked()) {
                throw new RuntimeException("このユーザーはロックされています");
            }

            user.setLocked(true);
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            System.err.println("ロック中にエラーが発生しました: " + e.getMessage());
            return false;
        }
    }

    // アカウントのロックを解除（アクセス許可）
    @Transactional
    public boolean unlockUser(Integer id) {
        try {
            UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

            if (!user.isLocked()) {
                throw new RuntimeException("このユーザーはロックされていません");
            }

            user.setLocked(false);
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            System.err.println("アンロック中にエラーが発生しました: " + e.getMessage());
            return false;
        }
    }

    // ユーザーの権限切替
    @Transactional
    public boolean toggleUserRole(Integer id) {
        try {
            UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
            
            if (user.isDeleted()) {
                throw new RuntimeException("削除されたユーザーのロールは変更できません");
            }

            //Role切替
            if ("ROLE_ADMIN".equals(user.getRole())) {
                user.setRole("ROLE_USER");
            } else {
                user.setRole("ROLE_ADMIN");
            }

            userRepository.save(user);
            return true;
        } catch (Exception e) {
            System.err.println("ロール変更中にエラーが発生しました: " + e.getMessage());
            return false;
        }
    }

    // ページネーション対応
    public Page<UserInfo> getUsersByPage(Pageable pageable) {
        return userRepository.findByIsDeletedFalse(pageable); // 論理削除されていないユーザーのみ
    }

    // 画像ファイルのアップロード処理
    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        if (profileImage != null && !profileImage.isEmpty()) {
            String uploadDir = "src/main/resources/static/uploads/";
            String imageFileName = profileImage.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
    
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
    
            Path filePath = uploadPath.resolve(imageFileName);
            profileImage.transferTo(filePath.toFile());
    
            return imageFileName;
        }
        return null;
    }
 
}