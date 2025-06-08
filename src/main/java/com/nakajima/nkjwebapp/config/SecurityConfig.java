package com.nakajima.nkjwebapp.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
        .userDetailsService(userDetailsService)
        .formLogin(login -> login
            .loginProcessingUrl("/login") // ログイン処理URL
            .loginPage("/login")          // ログインページ
            .successHandler(customAuthenticationSuccessHandler()) // 成功時処理
            .failureHandler(customAuthenticationFailureHandler()) // 失敗時処理（ロック対応）
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll()
        )
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers("/uploads/**").permitAll()
            .requestMatchers("/").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/contact_form").permitAll()
            .requestMatchers("/profile/**").permitAll()
            .requestMatchers("/user_list").permitAll()
            .requestMatchers("/locked").permitAll()
            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) -> {
            if (exception instanceof LockedException) {
                // アカウントがロックされているときのエラーメッセージ
                response.sendRedirect("/login?locked");
            } else {
                // 通常時のエラーメッセージ
                response.sendRedirect("/login?error");
            }
        };
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                UserInfo user = (UserInfo) authentication.getPrincipal();
                if (user.isAdmin()) {
                    getRedirectStrategy().sendRedirect(request, response, "/admin");
                } else {
                    getRedirectStrategy().sendRedirect(request, response, "/top");
                }
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
