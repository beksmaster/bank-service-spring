package com.example.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		System.out.println(		System.getenv("DB_PASSWORD"));
		SpringApplication.run(BankApplication.class, args);
	}

}
