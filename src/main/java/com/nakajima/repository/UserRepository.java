package com.nakajima.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nakajima.nkjwebapp.model.UserInfo;

@Repository //これを書き忘れてたかも
public interface UserRepository extends JpaRepository<UserInfo, Integer>{
    Optional<UserInfo> findByEmail(String email);  //カスタムメソッド、メールアドレスで検索する
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