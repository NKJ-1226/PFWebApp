package com.nakajima.nkjwebapp.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.core.userdetails.UserDetailsService; //インポート不足
////import com.nakajima.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    //おそらくここでUserDetailsService の二重設定していたから無限ループ起きてログインできなかったっぽい
    //private final CustomUserDetailsService customUserDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.formLogin(login -> login
            .loginProcessingUrl("/login") // ログインページ
            .loginPage("/login") // ログインページの指定
            .defaultSuccessUrl("/user", true) // ログイン成功時のリダイレクトページ
            .failureUrl("/login?error") // ログイン失敗時のページ
            .permitAll() // 誰でもアクセス可能
        ).logout(logout -> logout
            .logoutUrl("/logout") // ログアウト時URL
            .logoutSuccessUrl("/") // ログアウト成功時の遷移ページ
            .permitAll() // ログアウトは誰でもアクセス可能
            
        ).authorizeHttpRequests(authz -> authz
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 静的リソースは許可
            .requestMatchers("/").permitAll() // ホームページは許可
            .anyRequest().authenticated() // その他は認証必須
        );
        return http.build();
    }

    //おそらくここでUserDetailsService の二重設定していたから無限ループ起きてログインできなかったみたい
    //@Bean
    //public UserDetailsService userDetailsService() {
    //    return customUserDetailsService;
    //}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
        //return new BCryptPasswordEncoder(); ハッシュ化を一旦やめる
    }
}
