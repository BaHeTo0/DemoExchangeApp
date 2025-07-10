package me.BaHeTo0.demoExchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoExchangeApplication.class, args);
	}

}
