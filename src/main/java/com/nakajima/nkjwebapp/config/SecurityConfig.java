package com.nakajima.nkjwebapp.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nakajima.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
            .loginProcessingUrl("/login") // ログインページ
            .loginPage("/login") // ログインページの指定
            .defaultSuccessUrl("/user", true) // ログイン成功した際にリダイレクトするページ
            .failureUrl("/login?error") // ログイン失敗したときに移動するページ
            .permitAll() // このページは誰でも許可
        ).logout(logout -> logout
            .logoutSuccessUrl("/") // ログアウト成功したときに遷移するページ
        ).authorizeHttpRequests(authz -> authz
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 静的なものはすべて権限なしでも許可する
            .requestMatchers("/").permitAll() // 権限なしでも許可する
            .anyRequest().authenticated() // ルールにないものは常に認証が必要
        );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService(passwordEncoder);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}