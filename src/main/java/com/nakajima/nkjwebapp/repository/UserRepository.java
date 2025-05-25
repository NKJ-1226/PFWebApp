package com.nakajima.nkjwebapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nakajima.nkjwebapp.model.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Integer> {
    
    // 削除されていないユーザーを取得する
    List<UserInfo> findByIsDeletedFalse(); 

    // 削除済みのユーザーを取得
    List<UserInfo> findByIsDeletedTrue();

    // メールアドレスで検索（削除されていないユーザーのみ）
    Optional<UserInfo> findByEmailAndIsDeletedFalse(String email);

    // ユーザー名で検索（削除されていないユーザーのみ）
    Optional<UserInfo> findByUsernameAndIsDeletedFalse(String username);

    // 削除されていないユーザーをページネーション付きで取得
    Page<UserInfo> findByIsDeletedFalse(Pageable pageable);
}



/*
* Repositoryのインターフェースを作成
* Spring Data JPAのJpaRepositoryを継承することで、自動的にCRUD処理を実装できる。
* 
* JpaRepositoryが提供するメゾット(デフォで使える)有能！
* findAll() 全件取得する
* findById([データ型] [テーブルの名前]) 例）findById(int id)
* save(User user) 新規登録または更新する
* count() 全部の件数を取得する
*/