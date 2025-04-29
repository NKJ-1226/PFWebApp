package com.nakajima.nkjwebapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ここで静的リソースの設定をする
        registry.addResourceHandler("/uploads/**")  // アクセスURLとして"/uploads/**"を使う
                .addResourceLocations("file:/C:/WorkSpace/PF_NAKAJIMA/nkjwebapp/uploads/");  // 実際のアップロードディレクトリのフルパス
    }
}
