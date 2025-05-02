package com.nakajima.nkjwebapp.repository;

import com.nakajima.nkjwebapp.model.Contact;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    // 削除フラグが立ってない問い合わせだけ表示
    List<Contact> findByDeletedFalse();

    Optional<Contact> findByIdAndDeletedFalse(Integer id);

    // 削除フラグが立っているお問い合わせの取得
    List<Contact> findByDeletedTrue();
}
