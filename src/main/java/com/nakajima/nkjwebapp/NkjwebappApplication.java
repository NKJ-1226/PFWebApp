package com.nakajima.nkjwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.nakajima") //ここ追加した
@EnableJpaRepositories(basePackages = "com.nakajima.repository") //ここ追加し他
public class NkjwebappApplication {
	public static void main(String[] args) {
		SpringApplication.run(NkjwebappApplication.class, args);
	}

}
