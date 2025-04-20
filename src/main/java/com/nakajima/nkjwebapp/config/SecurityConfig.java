package com.nakajima.nkjwebapp.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.nakajima.nkjwebapp.model.UserInfo;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.csrf(csrf -> csrf.disable())  // CSRF保護を無効化
        .userDetailsService(userDetailsService) // ★ ここを追加！
        .formLogin(login -> login
            .loginProcessingUrl("/login") // ログイン処理
            .loginPage("/login") // ログインページ
            .successHandler(customAuthenticationSuccessHandler()) // ログイン成功時のリダイレクト
            .failureUrl("/login?error") // ログイン失敗時
            .permitAll()
        ).logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll()
        ).authorizeHttpRequests(authz -> authz
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 静的リソース許可
            .requestMatchers("/").permitAll() // ホームページ許可
            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // 管理者のみアクセス可能
            .anyRequest().authenticated() // その他は認証必須
        );

        return http.build();
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                UserInfo user = (UserInfo) authentication.getPrincipal();
                if (user.isAdmin()) {
                    getRedirectStrategy().sendRedirect(request, response, "/admin"); // 管理者ならこっち
                } else {
                    getRedirectStrategy().sendRedirect(request, response, "/top"); // 一般ユーザーならこっち
                }
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}