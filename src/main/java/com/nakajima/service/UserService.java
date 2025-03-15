package com.nakajima.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nakajima.nkjwebapp.model.UserInfo;
import com.nakajima.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    //全ユーザーの情報を取得する
    public List<UserInfo> getAllUsers(){
        return userRepository.findAll();
    }

    //ユーザーをidで取得する
    public UserInfo getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}
