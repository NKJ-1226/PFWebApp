package com.nakajima.nkjwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nakajima.nkjwebapp.repository")  // 正しいリポジトリパッケージを指定
public class NkjwebappApplication {
    public static void main(String[] args) {
        SpringApplication.run(NkjwebappApplication.class, args);
    }
}
