package com.sqzer.hsbctransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TransactionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionAppApplication.class, args);
	}

}
