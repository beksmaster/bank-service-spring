package com.example.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {System.out.println("URL=" + System.getenv("DB_URL"));
		System.out.println("USER=" + System.getenv("DB_USERNAME"));
		System.out.println("PASS=" + System.getenv("DB_PASSWORD"));
		SpringApplication.run(BankApplication.class, args);
	}

}
